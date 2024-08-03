import api from './api';

// 로그인
export const login = (memberId,password) => api.post(`/member/login?memberId=${memberId}&password=${password}`);

// 사용자 정보
export const info = () => api.get(`/member/info`);

// 회원 가입
export const join = (data) => api.post('/member',data);

// 회원 정보 수정
export const update = (data) => api.put('/member',data);

// 회원 탈퇴
export const remove = (memberId) => api.delete(`/member/${memberId}`);
