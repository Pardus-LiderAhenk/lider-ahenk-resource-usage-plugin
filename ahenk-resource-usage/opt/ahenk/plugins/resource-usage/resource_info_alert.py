#!/usr/bin/python
# -*- coding: utf-8 -*-
# Author: Cemre ALPSOY <cemre.alpsoy@agem.com.tr>

from base.plugin.abstract_plugin import AbstractPlugin
from base.system.system import System
from base.model.enum.ContentType import ContentType
import json


class ResourceUsage(AbstractPlugin):

    def __init__(self, data, context):
        super(AbstractPlugin, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()

    def handle_task(self):

        result = []
        items = (self.data)['items']
        print(System.Hardware.Network.ip_addresses()[0])
        for item in items:
            if (str(item['type']) == 'Memory' or str(item['type']) == 'Bellek') and System.Hardware.Memory.percent() > float(item['limit']):
                result.append("Memory Kullanımı Sınır Değeri Aşıldı.\r\nLimit Değeri: %"+item['limit']+"\r\nHesaplanılan Değer: %"+str(System.Hardware.Memory.percent())+"\r\n")
                result.append(item['email'])
            if str(item['type']) == 'Disk' and float(item['limit']) < System.Hardware.Disk.percent():
                result.append("Disk Kullanımı Sınır Değeri Aşıldı.\r\nLimit Değeri: %"+item['limit']+"\r\nHesaplanılan Değer: %"+str(System.Hardware.Disk.percent())+"\r\n")
                result.append(item['email'])

            """
            #TODO PSUTIL 4.2.0
            if str(item['type']) == 'CPU' and float(item['limit']) < System.Hardware.Cpu.cpu_percent():
                result.append("CPU Kullanımı Sınır Değeri Aşıldı.\r\nLimit Değeri: %"+item['limit']+"\r\nHesaplanılan Değer: %"+str(System.Hardware.Cpu.cpu_percent())+"\r\n")
                result.append(item['email'])
            """

        data = {'Result': result}
        print(data)
        self.context.create_response(code=self.message_code.TASK_PROCESSED.value, message='resource-usage-alert-response',
                                     data=data, content_type=ContentType.APPLICATION_JSON.value)


def handle_task(task, context):
    print('ResourceUsage Plugin Alert Task')
    print('Task Data : {}'.format(str(task)))
    plugin = ResourceUsage(task, context)
    plugin.handle_task()