#!/usr/bin/python
# -*- coding: utf-8 -*-
# Author: Cemre ALPSOY <cemre.alpsoy@agem.com.tr>
# Author: Emre Akkaya <emre.akkaya@agem.com.tr>

from base.plugin.abstract_plugin import AbstractPlugin
from base.system.system import System
from base.model.enum.ContentType import ContentType


class ResourceUsage(AbstractPlugin):
    def __init__(self, data, context):
        super(AbstractPlugin, self).__init__()
        self.data = data
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()
        self.MAX_NET_USAGE = 400000
        self.MAX_ATTACKS = 4

    def handle_task(self):
        try:
            device = ""
            self.logger.debug("[RESOURCE USAGE] Gathering resource usage for disk, memory and CPU.")
            for part in System.Hardware.Disk.partitions():
                if len(device) != 0:
                    device += ", "
                device = device + part.device
            data = {'System': System.Os.name(), 'Release': System.Os.kernel_release(),
                    'Version': System.Os.distribution_version(), 'Machine': System.Os.architecture(),
                    'CPU Physical Core Count': System.Hardware.Cpu.physical_core_count(),
                    'Total Memory': System.Hardware.Memory.total(),
                    'Usage': System.Hardware.Memory.used(),
                    'Total Disc': System.Hardware.Disk.total(),
                    'Usage Disc': System.Hardware.Disk.used(),
                    'Processor': System.Hardware.Cpu.brand(),
                    'Device': device,
                    'CPU Logical Core Count': System.Hardware.Cpu.logical_core_count(),
                    'CPU Actual Hz':  System.Hardware.Cpu.hz_actual(),
                    'CPU Advertised Hz': System.Hardware.Cpu.hz_advertised()
                    }
            self.logger.debug("[RESOURCE USAGE] Resource usage info gathered.")
            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='Anlık kaynak kullanım bilgisi başarıyla toplandı.',
                                         data=data, content_type=ContentType.APPLICATION_JSON.value)
        except Exception as e:
            self.logger.error(str(e))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='Anlık kaynak kullanım bilgisi toplanırken hata oluştu:' + str(e),
                                         content_type=ContentType.APPLICATION_JSON.value)


def handle_task(task, context):
    plugin = ResourceUsage(task, context)
    plugin.handle_task()
