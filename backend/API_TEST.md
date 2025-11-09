# 学生信息管理 API 测试文档

## 基础信息
- 服务器地址: `http://localhost:8080`
- API 基础路径: `/api/students`

## API 接口列表

### 1. 创建学生
**POST** `/api/students`

**请求体示例:**
```json
{
  "studentId": "2021001",
  "name": "张三",
  "gender": "男",
  "major": "计算机科学与技术",
  "grade": "2021级",
  "className": "计科2101",
  "gpa": 3.85,
  "rankingPercent": 0.15,
  "foreignLanguageScore": 550,
  "foreignLanguageType": "CET-6",
  "eligibleForExemption": true,
  "applicationStatus": "草稿"
}
```

**响应示例:**
```json
{
  "id": 1,
  "studentId": "2021001",
  "name": "张三",
  ...
}
```

---

### 2. 查询所有学生
**GET** `/api/students`

**响应示例:**
```json
[
  {
    "id": 1,
    "studentId": "2021001",
    "name": "张三",
    ...
  }
]
```

---

### 3. 根据ID查询学生
**GET** `/api/students/{id}`

**示例:** `GET /api/students/1`

---

### 4. 根据学号查询学生
**GET** `/api/students/studentId/{studentId}`

**示例:** `GET /api/students/studentId/2021001`

---

### 5. 根据姓名模糊查询
**GET** `/api/students/search/name?name={name}`

**示例:** `GET /api/students/search/name?name=张`

---

### 6. 根据专业查询
**GET** `/api/students/search/major?major={major}`

**示例:** `GET /api/students/search/major?major=计算机科学与技术`

---

### 7. 根据年级查询
**GET** `/api/students/search/grade?grade={grade}`

**示例:** `GET /api/students/search/grade?grade=2021级`

---

### 8. 根据申请状态查询
**GET** `/api/students/search/status?status={status}`

**示例:** `GET /api/students/search/status?status=草稿`

**状态值:** 草稿、已提交、审核中、通过、驳回

---

### 9. 更新学生信息
**PUT** `/api/students/{id}`

**请求体示例:**
```json
{
  "name": "张三（更新）",
  "gpa": 3.90,
  "applicationStatus": "已提交"
}
```

---

### 10. 删除学生
**DELETE** `/api/students/{id}`

**示例:** `DELETE /api/students/1`

---

### 11. 检查学号是否存在
**GET** `/api/students/check/studentId/{studentId}`

**示例:** `GET /api/students/check/studentId/2021001`

**响应示例:**
```json
{
  "exists": true,
  "studentId": "2021001"
}
```

---

## 使用 curl 测试示例

### 创建学生
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "2021001",
    "name": "张三",
    "gender": "男",
    "major": "计算机科学与技术",
    "grade": "2021级",
    "gpa": 3.85
  }'
```

### 查询所有学生
```bash
curl http://localhost:8080/api/students
```

### 根据ID查询
```bash
curl http://localhost:8080/api/students/1
```

### 更新学生
```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三（更新）",
    "gpa": 3.90
  }'
```

### 删除学生
```bash
curl -X DELETE http://localhost:8080/api/students/1
```

---

## 错误响应格式

当发生错误时，响应格式如下：
```json
{
  "error": "错误信息描述"
}
```

**常见错误:**
- `400 Bad Request`: 学号已存在
- `404 Not Found`: 学生不存在

