# 保研加分系统 API 文档

## 1. 学生信息接口

### 1.1 获取所有学生信息

- **接口 URL**: `/api/students`
- **方法**: GET
- **请求参数**: 无
- **响应体 JSON 示例**:
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

- **返回字段含义说明**:
  - `id`: 学生唯一标识符
  - `studentId`: 学号
  - `name`: 姓名
  - `gender`: 性别
  - `major`: 专业
  - `grade`: 年级
  - `className`: 班级
  - `gpa`: 绩点
  - `rankingPercent`: 排名百分比
  - `foreignLanguageScore`: 外语成绩
  - `foreignLanguageType`: 外语类型
  - `eligibleForExemption`: 是否符合推免资格
  - `papersA`: A类论文数量
  - `papersB`: B类论文数量
  - `papersC`: C类论文数量
  - `patentInventionCount`: 发明专利数量
  - `patentUtilityCount`: 实用新型专利数量
  - `firstAuthorCount`: 第一作者数量
  - `ccfCspScore`: CSP成绩
  - `academicBonus`: 学术加分
  - `competitionLevel`: 竞赛级别
  - `competitionAward`: 竞赛奖项
  - `competitionName`: 竞赛名称
  - `isTeamProject`: 是否团队项目
  - `teamRank`: 团队排名
  - `competitionBonus`: 竞赛加分
  - `innovationProjects`: 创新项目数量
  - `innovationLevel`: 创新项目级别
  - `volunteerHours`: 志愿服务时长
  - `honorTitles`: 荣誉称号
  - `honorBonus`: 荣誉加分
  - `socialWorkBonus`: 社会工作加分
  - `volunteerBonus`: 志愿服务加分
  - `sportsBonus`: 体育加分
  - `overallPerformanceBonus`: 综合表现加分
  - `academicScore`: 学术成绩
  - `academicSpecialtyScore`: 学术特长成绩
  - `comprehensivePerformanceScore`: 综合表现成绩
  - `totalScore`: 总成绩
  - `isSpecialExemption`: 是否特殊推免
  - `specialExemptionReason`: 特殊推免原因
  - `applicationStatus`: 申请状态
  - `reviewStatus`: 审核状态
  - `reviewComments`: 审核意见
  - `publicityRound`: 公示轮次
  - `isPublicized`: 是否已公示
  - `finalRanking`: 最终排名
  - `finalApproved`: 是否最终批准
  - `createTime`: 创建时间
  - `updateTime`: 更新时间

### 1.2 根据ID获取单个学生信息

- **接口 URL**: `/api/students/{id}`
- **方法**: GET
- **路径参数**: 
  - `id`: 学生ID
- **响应体 JSON 示例**:
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

- **返回字段含义说明**: 同上

## 2. 论文记录接口

### 2.1 获取学生论文记录

- **接口 URL**: `/api/students/{id}/papers`
- **方法**: GET
- **路径参数**: 
  - `id`: 学生ID
- **响应体 JSON 示例**:
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
  },
  {
    "id": "2",
    "title": "分布式系统负载均衡算法优化",
    "level": "B类",
    "publication": "软件学报",
    "publishDate": "2023-07-20",
    "authorOrder": 2,
    "totalAuthors": 4,
    "unitIsFirstUnit": true,
    "baseScore": 5,
    "computedScore": 4,
    "evidenceFiles": [
      "/uploads/papers/20210001_paper_2.pdf"
    ],
    "createTime": "2023-09-05T11:30:00",
    "updateTime": "2023-10-20T09:15:00"
  }
]
```

- **返回字段含义说明**:
  - `id`: 论文记录唯一标识符
  - `title`: 论文标题
  - `level`: 论文级别（A类/B类/C类）
  - `publication`: 发表期刊/会议名称
  - `publishDate`: 发表日期
  - `authorOrder`: 作者序号
  - `totalAuthors`: 总作者数
  - `unitIsFirstUnit`: 是否第一单位
  - `baseScore`: 基础分数
  - `computedScore`: 计算分数
  - `evidenceFiles`: 证明文件路径列表
  - `createTime`: 创建时间
  - `updateTime`: 更新时间

### 2.2 添加学生论文记录

- **接口 URL**: `/api/students/{id}/papers`
- **方法**: POST
- **路径参数**: 
  - `id`: 学生ID
- **请求体 JSON 示例**:
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

- **响应体 JSON 示例**:
```json
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
```

- **返回字段含义说明**: 同上

## 3. 专利记录接口

### 3.1 获取学生专利记录

- **接口 URL**: `/api/students/{id}/patents`
- **方法**: GET
- **路径参数**: 
  - `id`: 学生ID
- **响应体 JSON 示例**:
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
  },
  {
    "id": "2",
    "title": "一种智能物联网数据采集装置",
    "type": "实用新型专利",
    "authorizationDate": "2023-08-10",
    "isFirstAuthor": false,
    "baseScore": 5,
    "computedScore": 3,
    "evidenceFiles": [
      "/uploads/patents/20210001_patent_2.pdf"
    ],
    "createTime": "2023-09-05T11:30:00",
    "updateTime": "2023-10-20T09:15:00"
  }
]
```

- **返回字段含义说明**:
  - `id`: 专利记录唯一标识符
  - `title`: 专利标题
  - `type`: 专利类型（发明专利/实用新型专利/外观设计专利）
  - `authorizationDate`: 授权日期
  - `isFirstAuthor`: 是否第一发明人
  - `baseScore`: 基础分数
  - `computedScore`: 计算分数
  - `evidenceFiles`: 证明文件路径列表
  - `createTime`: 创建时间
  - `updateTime`: 更新时间

### 3.2 添加学生专利记录

- **接口 URL**: `/api/students/{id}/patents`
- **方法**: POST
- **路径参数**: 
  - `id`: 学生ID
- **请求体 JSON 示例**:
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

- **响应体 JSON 示例**:
```json
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
```

- **返回字段含义说明**: 同上

## 4. 竞赛记录接口

### 4.1 获取学生竞赛记录

- **接口 URL**: `/api/students/{id}/competitions`
- **方法**: GET
- **路径参数**: 
  - `id`: 学生ID
- **响应体 JSON 示例**:
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
  },
  {
    "id": "2",
    "name": "数学建模竞赛",
    "level": "省级",
    "award": "二等奖",
    "isTeamProject": true,
    "teamRank": 2,
    "awardDate": "2023-07-15",
    "baseScore": 10,
    "computedScore": 8,
    "evidenceFiles": [
      "/uploads/competitions/20210001_competition_2.pdf"
    ],
    "createTime": "2023-09-05T11:30:00",
    "updateTime": "2023-10-20T09:15:00"
  }
]
```

- **返回字段含义说明**:
  - `id`: 竞赛记录唯一标识符
  - `name`: 竞赛名称
  - `level`: 竞赛级别（国家级/省级/校级）
  - `award`: 获奖等级
  - `isTeamProject`: 是否团队项目
  - `teamRank`: 团队排名
  - `awardDate`: 获奖日期
  - `baseScore`: 基础分数
  - `computedScore`: 计算分数
  - `evidenceFiles`: 证明文件路径列表
  - `createTime`: 创建时间
  - `updateTime`: 更新时间

### 4.2 添加学生竞赛记录

- **接口 URL**: `/api/students/{id}/competitions`
- **方法**: POST
- **路径参数**: 
  - `id`: 学生ID
- **请求体 JSON 示例**:
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

- **响应体 JSON 示例**:
```json
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
```

- **返回字段含义说明**: 同上

## 5. 荣誉记录接口

### 5.1 获取学生荣誉记录

- **接口 URL**: `/api/students/{id}/honors`
- **方法**: GET
- **路径参数**: 
  - `id`: 学生ID
- **响应体 JSON 示例**:
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
  },
  {
    "id": "2",
    "title": "三好学生",
    "level": "院级",
    "date": "2023-09-10",
    "baseScore": 3,
    "computedScore": 3,
    "evidenceFiles": [
      "/uploads/honors/20210001_honor_2.pdf"
    ],
    "createTime": "2023-09-05T11:30:00",
    "updateTime": "2023-10-20T09:15:00"
  }
]
```

- **返回字段含义说明**:
  - `id`: 荣誉记录唯一标识符
  - `title`: 荣誉称号
  - `level`: 荣誉级别（国家级/省级/校级/院级）
  - `date`: 获得日期
  - `baseScore`: 基础分数
  - `computedScore`: 计算分数
  - `evidenceFiles`: 证明文件路径列表
  - `createTime`: 创建时间
  - `updateTime`: 更新时间

### 5.2 添加学生荣誉记录

- **接口 URL**: `/api/students/{id}/honors`
- **方法**: POST
- **路径参数**: 
  - `id`: 学生ID
- **请求体 JSON 示例**:
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

- **响应体 JSON 示例**:
```json
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
```

- **返回字段含义说明**: 同上

## 6. 审核记录接口

### 6.1 获取学生审核记录

- **接口 URL**: `/api/students/{id}/reviews`
- **方法**: GET
- **路径参数**: 
  - `id`: 学生ID
- **响应体 JSON 示例**:
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
  },
  {
    "id": "2",
    "reviewerName": "王教授",
    "comments": "论文质量较高，但专利材料需要补充",
    "status": "待补充",
    "reviewDate": "2023-10-20",
    "round": 2,
    "createTime": "2023-10-20T09:15:00",
    "updateTime": "2023-10-20T09:15:00"
  }
]
```

- **返回字段含义说明**:
  - `id`: 审核记录唯一标识符
  - `reviewerName`: 审核人姓名
  - `comments`: 审核意见
  - `status`: 审核状态（通过/不通过/待补充）
  - `reviewDate`: 审核日期
  - `round`: 审核轮次
  - `createTime`: 创建时间
  - `updateTime`: 更新时间

### 6.2 添加学生审核记录

- **接口 URL**: `/api/students/{id}/reviews`
- **方法**: POST
- **路径参数**: 
  - `id`: 学生ID
- **请求体 JSON 示例**:
```json
{
  "reviewerName": "李老师",
  "comments": "材料齐全，符合要求",
  "status": "通过",
  "reviewDate": "2023-10-15",
  "round": 1
}
```

- **响应体 JSON 示例**:
```json
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
```

- **返回字段含义说明**: 同上

## 7. 统一申请记录接口

### 7.1 获取学生所有申请记录

- **接口 URL**: `/api/students/{id}/applications`
- **方法**: GET
- **路径参数**: 
  - `id`: 学生ID
- **响应体 JSON 示例**:
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
  },
  {
    "id": "2",
    "studentId": "20210001",
    "studentName": "张三",
    "type": "竞赛",
    "title": "ACM程序设计竞赛",
    "description": "参加ACM程序设计竞赛并获得国家级一等奖",
    "points": 20,
    "status": "approved",
    "submittedAt": "2023-09-05T11:30:00",
    "reviewedAt": "2023-10-20T09:15:00",
    "reviewComment": "竞赛成绩优异，符合加分标准",
    "files": [
      "/uploads/competitions/20210001_competition_1.pdf",
      "/uploads/competitions/20210001_competition_1_certificate.jpg"
    ]
  },
  {
    "id": "3",
    "studentId": "20210001",
    "studentName": "张三",
    "type": "专利",
    "title": "一种基于深度学习的图像识别系统",
    "description": "发明专利，主要应用于智能图像识别领域",
    "points": 15,
    "status": "pending",
    "submittedAt": "2023-10-25T16:45:00",
    "reviewedAt": null,
    "reviewComment": null,
    "files": [
      "/uploads/patents/20210001_patent_1.pdf",
      "/uploads/patents/20210001_patent_1_certificate.pdf"
    ]
  }
]
```

- **返回字段含义说明**:
  - `id`: 申请记录唯一标识符
  - `studentId`: 学生学号
  - `studentName`: 学生姓名
  - `type`: 申请类型（论文/专利/竞赛/荣誉）
  - `title`: 申请标题
  - `description`: 申请描述
  - `points`: 申请分值
  - `status`: 申请状态（pending/approved/rejected）
  - `submittedAt`: 提交时间
  - `reviewedAt`: 审核时间
  - `reviewComment`: 审核意见
  - `files`: 附件文件路径列表

### 7.2 添加学生申请记录

- **接口 URL**: `/api/students/{id}/applications`
- **方法**: POST
- **路径参数**: 
  - `id`: 学生ID
- **请求体 JSON 示例**:
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

- **响应体 JSON 示例**:
```json
{
  "id": "1",
  "studentId": "20210001",
  "studentName": "张三",
  "type": "论文",
  "title": "基于深度学习的图像识别技术研究",
  "description": "发表在计算机学报的A类论文，主要研究基于深度学习的图像识别技术",
  "points": 10,
  "status": "pending",
  "submittedAt": "2023-09-01T10:00:00",
  "reviewedAt": null,
  "reviewComment": null,
  "files": [
    "/uploads/papers/20210001_paper_1.pdf",
    "/uploads/papers/20210001_paper_1_supplement.pdf"
  ]
}
```

- **返回字段含义说明**: 同上