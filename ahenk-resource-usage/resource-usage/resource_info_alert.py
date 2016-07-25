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
        self.logger.debug("Handling Resource Info Alert Task")
        try:
            result = []
            items = self.data['items']
            for item in items:
                if (str(item['type']) == 'Memory' or str(
                        item['type']) == 'Bellek') and System.Hardware.Memory.percent() > float(item['limit']):
                    if str(item['type']) == 'Memory':
                        result.append("Memory Usage has gone over the limit value .\r\nLimit Value: %" + item[
                            'limit'] + "\r\nCalculated Value: %" + str(
                            System.Hardware.Memory.percent()) + "\r\nAhenk Ip Adresses:" + str(
                            System.Hardware.Network.ip_addresses()) + "\r\nAhenk Mac Adresses:" + str(
                            System.Hardware.Network.mac_addresses()))
                    else:
                        result.append("Memory Kullanımı Sınır Değeri Aşıldı.\r\nLimit Değeri: %" + item[
                            'limit'] + "\r\nHesaplanılan Değer: %" + str(
                            System.Hardware.Memory.percent()) + "\r\nAhenk Ip Adresleri:" + str(
                            System.Hardware.Network.ip_addresses()) + "\r\nAhenk Mac Adresleri:" + str(
                            System.Hardware.Network.mac_addresses()))
                    result.append(item['email'])
                if str(item['type']) == 'Disc' and float(item['limit']) < System.Hardware.Disk.percent():
                    if str(item['type']) == 'Disc':
                        result.append("Disk Usage has gone over the limit value.\r\nLimit Value: %" + item[
                        'limit'] + "\r\nCalculated Value: %" + str(
                        System.Hardware.Disk.percent()) + "\r\nAhenk Ip Adresses:" + str(
                        System.Hardware.Network.ip_addresses()) + "\r\nAhenk Mac Adresses:" + str(
                        System.Hardware.Network.mac_addresses()))
                    else:
                        result.append("Disk Kullanımı Sınır Değeri Aşıldı.\r\nLimit Değeri: %" + item[
                        'limit'] + "\r\nHesaplanılan Değer: %" + str(
                        System.Hardware.Disk.percent()) + "\r\nAhenk Ip Adresleri:" + str(
                        System.Hardware.Network.ip_addresses()) + "\r\nAhenk Mac Adresleri:" + str(
                        System.Hardware.Network.mac_addresses()))
                    result.append(item['email'])
            self.logger.debug("Limit Values are checked")
            data = {'Result': result}
            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='resource-usage-alert-response',
                                         data=json.dumps(data), content_type=ContentType.APPLICATION_JSON.value)
            self.logger.debug("Task processed successfully")
        except Exception as e:
            self.logger.debug(str(e))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='Error in Resource Info Alert Task ',
                                         content_type=ContentType.APPLICATION_JSON.value)


def handle_task(task, context):
    print('RESOURCE USAGE ALERT TASK')
    plugin = ResourceUsage(task, context)
    plugin.handle_task()
    print('RESOURCE USAGE ALERT TASK HANDLED')
