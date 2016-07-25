#!/usr/bin/python
# -*- coding: utf-8 -*-
# Author: Cemre ALPSOY <cemre.alpsoy@agem.com.tr>
# Author: Emre Akkaya <emre.akkaya@agem.com.tr>

from base.plugin.abstract_plugin import AbstractPlugin
from base.model.enum.ContentType import ContentType
from threading import Thread
import time
import psutil


class ResourceUsage(AbstractPlugin):
    def __init__(self, data, context):
        super(AbstractPlugin, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()
        self.is_running = False
        self._threadCep = None

    def handle_task(self):
        try:
            action = self.data['action']
            self.logger.debug("[RESOURCE USAGE] Action: {0}".format(action))

            if action == "start_timer":
                self.gather_resource_usage()
                self.is_running = True
                self._threadCep = Thread(target=self.run_timer,
                                         args=(int(self.data['interval']), self.context.get('task_id')))
                self._threadCep.start()
                self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                             message='Kaynak kullanım bilgileri toplanmaya başlandı.',
                                             content_type=ContentType.APPLICATION_JSON.value)
            elif action == "stop_timer":
                self.is_running = False
                self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                             message='Kaynak kullanım bilgilerinin toplanması durduruldu.',
                                             content_type=ContentType.APPLICATION_JSON.value)
        except Exception as e:
            self.logger.error(str(e))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='Kaynak kullanım bilgileri işlenirken hata oluştu: ' + str(e),
                                         content_type=ContentType.APPLICATION_JSON.value)

    def run_timer(self, interval, task_id):
        while self.is_running:
            self.gather_resource_usage(task_id)
            time.sleep(interval)

    def gather_resource_usage(self, task_id):
        # Memory usage
        memory_usage = psutil.virtual_memory()
        self.logger.debug("[RESOURCE USAGE] Memory usage: {0}".format(memory_usage))
        # Disk usage
        disk_usage = psutil.disk_usage('/')
        self.logger.debug("[RESOURCE USAGE] Disk usage: {0}".format(disk_usage))
        # CPU usage
        cpu_percentage = psutil.cpu_percent(interval=1, percpu=True)
        self.logger.debug("[RESOURCE USAGE] CPU percentage: {0}".format(cpu_percentage))

        command = 'python3 /opt/ahenk/ahenkd.py send -t {0} -m "{{ \'memoryUsage\': {1}, \'diskUsage\': {2}, \'cpuPercentage\': {3} }}"'.format(task_id, memory_usage, disk_usage, cpu_percentage)
        result_code, p_out, p_err = self.execute(command)
        if result_code != 0:
            self.logger.error("[PACKAGE MANAGER] Error occurred while sending message: " + str(p_err))


def handle_task(task, context):
    plugin = ResourceUsage(task, context)
    plugin.handle_task()
