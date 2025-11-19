# 保研加分系统 API 测试文档

## 基础信息
- 服务器地址: `http://localhost:8080`
- API 基础路径: `/api/students`

## API 接口列表

### 1. 创建学生
**POST** `/api/students`

**请求体示例:**
```json
{
  "studentId": "20210001",
  "name": "张三",
  "gender": "男",
  "major": "计算机科学与技术",
  "grade": "2021级",
  "className": "计科2101班",
  "gpa": 3.85,
  "rankingPercent": 5.2,
  "foreignLanguageScore": 550,
  "foreignLanguageType": "CET-6",
  "eligibleForExemption": true,
  "papersA": 2,
  "papersB": 1,
  "papersC": 3,
  "patentInventionCount": 1,
  "patentUtilityCount": 2,
  "firstAuthorCount": 3,
  "ccfCspScore": 320,
  "academicBonus": 15.5,
  "competitionLevel": "国家级",
  "competitionAward": "一等奖",
  "competitionName": "ACM程序设计竞赛",
  "isTeamProject": true,
  "teamRank": 1,
  "competitionBonus": 20,
  "innovationProjects": 2,
  "innovationLevel": "国家级",
  "volunteerHours": 120,
  "honorTitles": "优秀学生干部",
  "honorBonus": 5,
  "socialWorkBonus": 3,
  "volunteerBonus": 4,
  "sportsBonus": 2,
  "overallPerformanceBonus": 8,
  "isSpecialExemption": false,
  "specialExemptionReason": "",
  "applicationStatus": "已提交"
}
```

**响应示例:**
```json
{
  "id": "1",
  "studentId": "20210001",
  "name": "张三",
  "gender": "男",
  "major": "计算机科学与技术",
  "grade": "2021级",
  "className": "计科2101班",
  "gpa": 3.85,
  "rankingPercent": 5.2,
  "foreignLanguageScore": 550,
  "foreignLanguageType": "CET-6",
  "eligibleForExemption": true,
  "papersA": 2,
  "papersB": 1,
  "papersC": 3,
  "patentInventionCount": 1,
  "patentUtilityCount": 2,
  "firstAuthorCount": 3,
  "ccfCspScore": 320,
  "academicBonus": 15.5,
  "competitionLevel": "国家级",
  "competitionAward": "一等奖",
  "competitionName": "ACM程序设计竞赛",
  "isTeamProject": true,
  "teamRank": 1,
  "competitionBonus": 20,
  "innovationProjects": 2,
  "innovationLevel": "国家级",
  "volunteerHours": 120,
  "honorTitles": "优秀学生干部",
  "honorBonus": 5,
  "socialWorkBonus": 3,
  "volunteerBonus": 4,
  "sportsBonus": 2,
  "overallPerformanceBonus": 8,
  "academicScore": 85.5,
  "academicSpecialtyScore": 20.5,
  "comprehensivePerformanceScore": 22,
  "totalScore": 128,
  "isSpecialExemption": false,
  "specialExemptionReason": "",
  "applicationStatus": "已提交",
  "reviewStatus": "审核中",
  "reviewComments": "",
  "publicityRound": 1,
  "isPublicized": false,
  "finalRanking": 0,
  "finalApproved": false,
  "createTime": "2023-09-01T10:00:00",
  "updateTime": "2023-10-15T14:30:00",
  "papers": [],
  "patents": [],
  "competitions": [],
  "honors": [],
  "reviews": []
}
```

---

### 2. 查询所有学生
**GET** `/api/students`

**响应示例:**
```json
[
  {
    "id": "1",
    "studentId": "20210001",
    "name": "张三",
    "gender": "男",
    "major": "计算机科学与技术",
    "grade": "2021级",
    "className": "计科2101班",
    "gpa": 3.85,
    "rankingPercent": 5.2,
    "foreignLanguageScore": 550,
    "foreignLanguageType": "CET-6",
    "eligibleForExemption": true,
    "papersA": 2,
    "papersB": 1,
    "papersC": 3,
    "patentInventionCount": 1,
    "patentUtilityCount": 2,
    "firstAuthorCount": 3,
    "ccfCspScore": 320,
    "academicBonus": 15.5,
    "competitionLevel": "国家级",
    "competitionAward": "一等奖",
    "competitionName": "ACM程序设计竞赛",
    "isTeamProject": true,
    "teamRank": 1,
    "competitionBonus": 20,
    "innovationProjects": 2,
    "innovationLevel": "国家级",
    "volunteerHours": 120,
    "honorTitles": "优秀学生干部",
    "honorBonus": 5,
    "socialWorkBonus": 3,
    "volunteerBonus": 4,
    "sportsBonus": 2,
    "overallPerformanceBonus": 8,
    "academicScore": 85.5,
    "academicSpecialtyScore": 20.5,
    "comprehensivePerformanceScore": 22,
    "totalScore": 128,
    "isSpecialExemption": false,
    "specialExemptionReason": "",
    "applicationStatus": "已提交",
    "reviewStatus": "审核中",
    "reviewComments": "",
    "publicityRound": 1,
    "isPublicized": false,
    "finalRanking": 0,
    "finalApproved": false,
    "createTime": "2023-09-01T10:00:00",
    "updateTime": "2023-10-15T14:30:00"
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

**示例:** `GET /api/students/studentId/20210001`

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

**示例:** `GET /api/students/search/status?status=已提交`

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

**示例:** `GET /api/students/check/studentId/20210001`

**响应示例:**
```json
{
  "exists": true,
  "studentId": "20210001"
}
```

---

## 子表接口

### 12. 获取学生论文记录
**GET** `/api/students/{id}/papers`

**示例:** `GET /api/students/1/papers`

**响应示例:**
```json
[
  {
    "id": "1",
    "title": "基于深度学习的图像识别技术研究",
    "level": "A类",
    "publication": "计算机学报",
    "publishDate": "2023-05-15",
    "authorOrder": 1,
    "totalAuthors": 3,
    "unitIsFirstUnit": true,
    "baseScore": 10,
    "computedScore": 10,
    "evidenceFiles": [
      "/uploads/papers/20210001_paper_1.pdf",
      "/uploads/papers/20210001_paper_1_supplement.pdf"
    ],
    "createTime": "2023-09-01T10:00:00",
    "updateTime": "2023-10-15T14:30:00"
  }
]
```

---

### 13. 添加学生论文记录
**POST** `/api/students/{id}/papers`

**请求体示例:**
```json
{
  "title": "基于深度学习的图像识别技术研究",
  "level": "A类",
  "publication": "计算机学报",
  "publishDate": "2023-05-15",
  "authorOrder": 1,
  "totalAuthors": 3,
  "unitIsFirstUnit": true,
  "evidenceFiles": [
    "/uploads/papers/20210001_paper_1.pdf",
    "/uploads/papers/20210001_paper_1_supplement.pdf"
  ]
}
```

---

### 14. 获取学生专利记录
**GET** `/api/students/{id}/patents`

**示例:** `GET /api/students/1/patents`

**响应示例:**
```json
[
  {
    "id": "1",
    "title": "一种基于深度学习的图像识别系统",
    "type": "发明专利",
    "authorizationDate": "2023-06-20",
    "isFirstAuthor": true,
    "baseScore": 15,
    "computedScore": 15,
    "evidenceFiles": [
      "/uploads/patents/20210001_patent_1.pdf",
      "/uploads/patents/20210001_patent_1_certificate.pdf"
    ],
    "createTime": "2023-09-01T10:00:00",
    "updateTime": "2023-10-15T14:30:00"
  }
]
```

---

### 15. 添加学生专利记录
**POST** `/api/students/{id}/patents`

**请求体示例:**
```json
{
  "title": "一种基于深度学习的图像识别系统",
  "type": "发明专利",
  "authorizationDate": "2023-06-20",
  "isFirstAuthor": true,
  "evidenceFiles": [
    "/uploads/patents/20210001_patent_1.pdf",
    "/uploads/patents/20210001_patent_1_certificate.pdf"
  ]
}
```

---

### 16. 获取学生竞赛记录
**GET** `/api/students/{id}/competitions`

**示例:** `GET /api/students/1/competitions`

**响应示例:**
```json
[
  {
    "id": "1",
    "name": "ACM程序设计竞赛",
    "level": "国家级",
    "award": "一等奖",
    "isTeamProject": true,
    "teamRank": 1,
    "awardDate": "2023-05-20",
    "baseScore": 20,
    "computedScore": 20,
    "evidenceFiles": [
      "/uploads/competitions/20210001_competition_1.pdf",
      "/uploads/competitions/20210001_competition_1_certificate.jpg"
    ],
    "createTime": "2023-09-01T10:00:00",
    "updateTime": "2023-10-15T14:30:00"
  }
]
```

---

### 17. 添加学生竞赛记录
**POST** `/api/students/{id}/competitions`

**请求体示例:**
```json
{
  "name": "ACM程序设计竞赛",
  "level": "国家级",
  "award": "一等奖",
  "isTeamProject": true,
  "teamRank": 1,
  "awardDate": "2023-05-20",
  "evidenceFiles": [
    "/uploads/competitions/20210001_competition_1.pdf",
    "/uploads/competitions/20210001_competition_1_certificate.jpg"
  ]
}
```

---

### 18. 获取学生荣誉记录
**GET** `/api/students/{id}/honors`

**示例:** `GET /api/students/1/honors`

**响应示例:**
```json
[
  {
    "id": "1",
    "title": "优秀学生干部",
    "level": "校级",
    "date": "2023-06-01",
    "baseScore": 5,
    "computedScore": 5,
    "evidenceFiles": [
      "/uploads/honors/20210001_honor_1.pdf",
      "/uploads/honors/20210001_honor_1_certificate.jpg"
    ],
    "createTime": "2023-09-01T10:00:00",
    "updateTime": "2023-10-15T14:30:00"
  }
]
```

---

### 19. 添加学生荣誉记录
**POST** `/api/students/{id}/honors`

**请求体示例:**
```json
{
  "title": "优秀学生干部",
  "level": "校级",
  "date": "2023-06-01",
  "evidenceFiles": [
    "/uploads/honors/20210001_honor_1.pdf",
    "/uploads/honors/20210001_honor_1_certificate.jpg"
  ]
}
```

---

### 20. 获取学生审核记录
**GET** `/api/students/{id}/reviews`

**示例:** `GET /api/students/1/reviews`

**响应示例:**
```json
[
  {
    "id": "1",
    "reviewerName": "李老师",
    "comments": "材料齐全，符合要求",
    "status": "通过",
    "reviewDate": "2023-10-15",
    "round": 1,
    "createTime": "2023-10-15T14:30:00",
    "updateTime": "2023-10-15T14:30:00"
  }
]
```

---

### 21. 添加学生审核记录
**POST** `/api/students/{id}/reviews`

**请求体示例:**
```json
{
  "reviewerName": "李老师",
  "comments": "材料齐全，符合要求",
  "status": "通过",
  "reviewDate": "2023-10-15",
  "round": 1
}
```

---

### 22. 获取学生所有申请记录
**GET** `/api/students/{id}/applications`

**示例:** `GET /api/students/1/applications`

**响应示例:**
```json
[
  {
    "id": "1",
    "studentId": "20210001",
    "studentName": "张三",
    "type": "论文",
    "title": "基于深度学习的图像识别技术研究",
    "description": "发表在计算机学报的A类论文，主要研究基于深度学习的图像识别技术",
    "points": 10,
    "status": "approved",
    "submittedAt": "2023-09-01T10:00:00",
    "reviewedAt": "2023-10-15T14:30:00",
    "reviewComment": "论文质量高，符合A类标准",
    "files": [
      "/uploads/papers/20210001_paper_1.pdf",
      "/uploads/papers/20210001_paper_1_supplement.pdf"
    ]
  }
]
```

---

### 23. 添加学生申请记录
**POST** `/api/students/{id}/applications`

**请求体示例:**
```json
{
  "type": "论文",
  "title": "基于深度学习的图像识别技术研究",
  "description": "发表在计算机学报的A类论文，主要研究基于深度学习的图像识别技术",
  "files": [
    "/uploads/papers/20210001_paper_1.pdf",
    "/uploads/papers/20210001_paper_1_supplement.pdf"
  ]
}
```

---

## 使用 curl 测试示例

### 创建学生
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "20210001",
    "name": "张三",
    "gender": "男",
    "major": "计算机科学与技术",
    "grade": "2021级",
    "className": "计科2101班",
    "gpa": 3.85,
    "rankingPercent": 5.2,
    "foreignLanguageScore": 550,
    "foreignLanguageType": "CET-6",
    "eligibleForExemption": true,
    "applicationStatus": "已提交"
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

### 添加论文记录
```bash
curl -X POST http://localhost:8080/api/students/1/papers \
  -H "Content-Type: application/json" \
  -d '{
    "title": "基于深度学习的图像识别技术研究",
    "level": "A类",
    "publication": "计算机学报",
    "publishDate": "2023-05-15",
    "authorOrder": 1,
    "totalAuthors": 3,
    "unitIsFirstUnit": true,
    "evidenceFiles": [
      "/uploads/papers/20210001_paper_1.pdf",
      "/uploads/papers/20210001_paper_1_supplement.pdf"
    ]
  }'
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

