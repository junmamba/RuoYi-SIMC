import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询表格列表
export function tableList(query) {
  return request({
    url: '/simc/oaic/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getOldAgriculturalInsuranceCancel(residentIdCardNo) {
  return request({
    url: '/simc/oaic/' + parseStrEmpty(residentIdCardNo),
    method: 'get'
  })
}

// 操作
export function operOldAgriculturalInsuranceCancel(data) {
  return request({
    url: '/simc/oaic/oper',
    method: 'post',
    data: data
  })
}

export function delOldAgriculturalInsuranceCancel(residentIdCardNos) {
  return request({
    url: '/simc/oaic/' + residentIdCardNos,
    method: 'delete'
  })
}
