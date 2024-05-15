import request from '@/utils/request'

// 查询表格列表
export function tableList(query) {
  return request({
    url: '/simc/rsis/list',
    method: 'get',
    params: query
  })
}

export function delResidentSocialInsuranceSubsidy(subsidyLogIds) {
  return request({
    url: '/simc/rsis/' + subsidyLogIds,
    method: 'delete'
  })
}
