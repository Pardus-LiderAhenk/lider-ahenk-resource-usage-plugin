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
        print('handle_task')

        device = ""
        for part in System.Hardware.Disk.partitions():
            if len(device) != 0:
                device += ", "
            device = device + part.device
        data = {'System': System.Os.name(), 'Release': System.Os.kernel_release(),
                'Version': System.Os.distribution_version(), 'Machine': System.Os.architecture(),
                'CPU Physical Core Count': System.Hardware.Cpu.physical_core_count(), 'Total Memory': System.Hardware.Memory.total(),
                'Usage': System.Hardware.Memory.used(), 'Total Disc': System.Hardware.Disk.total(),
                'Usage Disc': System.Hardware.Disk.used(), 'Processor': System.Hardware.Cpu.brand(), 'Device': device,
                'CPU Logical Core Count': System.Hardware.Cpu.logical_core_count(),
                'CPU Actual Hz': System.Hardware.Cpu.hz_actual(), 'CPU Advertised Hz': System.Hardware.Cpu.hz_advertised()}

        self.context.create_response(code=self.message_code.TASK_PROCESSED.value, message='resource-usage-response',
                                     data=data, content_type=ContentType.APPLICATION_JSON.value)


def handle_task(task, context):
    print('ResourceUsage Plugin Policy')
    print('Task Data : {}'.format(str(task)))
    plugin = ResourceUsage(task, context)
    plugin.handle_task()
