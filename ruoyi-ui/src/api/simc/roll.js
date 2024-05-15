import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询表格列表
export function tableList(query) {
  return request({
    url: '/simc/roll/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getResidentOldLandLosing(residentIdCardNo) {
  return request({
    url: '/simc/roll/' + parseStrEmpty(residentIdCardNo),
    method: 'get'
  })
}

// 操作
export function operResidentOldLandLosing(data) {
  return request({
    url: '/simc/roll/oper',
    method: 'post',
    data: data
  })
}

export function delResidentOldLandLosing(residentIdCardNos) {
  return request({
    url: '/simc/roll/' + residentIdCardNos,
    method: 'delete'
  })
}
