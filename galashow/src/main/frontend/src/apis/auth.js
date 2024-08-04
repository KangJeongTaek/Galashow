import api from './api';

// 로그인
export const login = (memberId,password) => api.post(`/login?memberId=${memberId}&password=${password}`);

// 사용자 정보
export const info = () => api.get(`/member/info`);

// 회원 가입
export const join = (data) => api.post('/member',data);

// 회원 정보 수정
export const update = (mid,data) => api.put(`/member/${mid}`,data);

// 회원 탈퇴
export const remove = (mid) => api.delete(`/member/${mid}`);
