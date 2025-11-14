import axios from 'axios'

const api = axios.create({
	baseURL: '/api'
})

export interface StudentPayload {
	studentId: string
	name: string
	gender?: string
	major?: string
	grade?: string
	className?: string
	gpa?: number
}

export const studentsApi = {
	list: () => api.get('/students'),
	get: (id: number) => api.get(`/students/${id}`),
	create: (data: StudentPayload) => api.post('/students', data),
	update: (id: number, data: Partial<StudentPayload>) => api.put(`/students/${id}`, data),
	remove: (id: number) => api.delete(`/students/${id}`),
	exists: (studentId: string) => api.get(`/students/check/studentId/${studentId}`)
}
