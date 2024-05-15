import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询表格列表
export function tableList(query) {
  return request({
    url: '/simc/rsi/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getResidentSocialInsurance(residentIdCardNo) {
  return request({
    url: '/simc/rsi/' + parseStrEmpty(residentIdCardNo),
    method: 'get'
  })
}

// 操作
export function operResidentSocialInsurance(data) {
  return request({
    url: '/simc/rsi/oper',
    method: 'post',
    data: data
  })
}
