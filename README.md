下面是一份为你们项目量身定制、**适合三人团队并行开发的完整版 README（含详细分支切换教程）**。
内容包含：项目说明、分支规范、协作流程、详细 Git 操作（命令行 + IDEA 中文界面）、冲突处理等。

---

# 🧭 保研加分小助手 — 团队协作与 Git 使用指南

> 本文档用于指导团队在 **Git + GitHub + IntelliJ IDEA + Vue** 环境下进行并行开发与版本管理。
> 项目目标：构建一个“保研加分小助手”网站，用于简化推免加分审核流程。
> 团队组成：2名后端（Java + Spring Boot） + 1名前端（Vue.js）。

---

## 📁 一、项目结构总览

```
baoyan-assistant/
├── src/
│   ├── main/java/com/example/baoyan_assistant/   # 后端 Java 源代码
│   │   └── entity/                               # 实体类（如 Student.java）
│   ├── resources/                                # 配置文件、模板等
│   │   ├── application.properties
│   │   ├── static/
│   │   └── templates/
│   └── test/                                     # 测试文件
├── pom.xml                                       # Maven 依赖管理文件
├── student.db                                    # SQLite 数据库文件
├── .gitignore                                    # Git 忽略规则
└── README.md                                     # 本文档
```

---

## 🧑‍💻 二、团队成员与职责分工

| 成员   | 职责          | 技术栈                         | 分支               |
| ---- | ----------- | --------------------------- | ---------------- |
| 你    | 后端主负责人、项目管理 | Java + Spring Boot + SQLite | `backend-core`   |
| B 同学 | 后端功能开发      | Java + Spring Boot          | `backend-module` |
| A 同学 | 前端开发        | Vue.js                      | `frontend`       |

---

## 🪜 三、分支管理策略（Git Flow 精简版）

### 主分支结构

```
main          ← 稳定版本（汇报演示使用）
│
└── dev       ← 集成开发分支
     ├── backend-core
     ├── backend-module
     └── frontend
```

### 命名规范

| 分支类型 | 命名示例                       | 说明      |
| ---- | -------------------------- | ------- |
| 主分支  | `main`                     | 最终稳定版本  |
| 开发主线 | `dev`                      | 各分支代码汇总 |
| 功能开发 | `backend-core`, `frontend` | 各成员工作区  |
| 临时实验 | `feature/student-api`      | 新功能测试分支 |

---

## ⚙️ 四、项目初始化流程

1. 由负责人（你）在 GitHub 上创建远程仓库；
2. 本地 IDEA 打开项目；
3. 通过 “VCS → 从版本控制中获取（或 Git → Clone）” 克隆到本地；
4. 初始化 Git：

   ```bash
   git init
   git remote add origin <你的GitHub仓库地址>
   git add .
   git commit -m "init: 项目初始化"
   git push origin main
   ```
5. 创建 `dev` 分支并推送：

   ```bash
   git checkout -b dev
   git push origin dev
   ```

---

## 🚀 五、如何转到自己的分支开发（详细教程）

> 这是你和队友最常用的操作。你可以用 **命令行** 或 **IntelliJ IDEA 中文界面** 完成。

---

### 🧱 方式一：命令行（推荐）

#### 1️⃣ 查看当前分支

```bash
git branch
```

输出示例：

```
* main
  dev
```

`*` 号表示当前所在分支。

---

#### 2️⃣ 基于 `dev` 创建并切换自己的分支

假设你是后端主负责人，要创建 `backend-core` 分支：

```bash
git checkout dev
git pull origin dev     # 先拉取最新开发分支
git checkout -b backend-core
```

看到提示：

```
Switched to a new branch 'backend-core'
```

说明创建并切换成功。

---

#### 3️⃣ 验证当前分支

```bash
git branch
```

输出：

```
  main
  dev
* backend-core
```

星号 (*) 表示当前分支。

---

#### 4️⃣ 在该分支写代码

此时你对项目的修改只影响 `backend-core` 分支。

---

#### 5️⃣ 提交修改并推送到 GitHub

```bash
git add .
git commit -m "feat: 完成学生实体类与数据库配置"
git push origin backend-core
```

执行后，GitHub 上会出现 `backend-core` 分支。

---

#### 6️⃣ 创建 Pull Request（PR）

在 GitHub 仓库：

* 打开 “Pull Requests” → “New Pull Request”
* 选择：

  * base: `dev`
  * compare: `backend-core`
* 填写改动说明 → 提交。

---

### 🧰 方式二：IntelliJ IDEA 中文界面操作

1️⃣ **打开右下角分支标签**（显示为 `main` 或 `dev`）
点击它。

2️⃣ **选择 “新建分支 (New Branch)”**
输入分支名，如：

```
backend-core
```

勾选“切换到新分支”，点击“创建”。

3️⃣ **确认切换成功**
右下角分支名称变为 `backend-core`。

4️⃣ **编写代码 → 提交推送**
顶部菜单：

> `Git → 提交 (Commit)` → 填写说明 → “提交并推送 (Commit and Push)”。

5️⃣ **到 GitHub 查看分支**
仓库页面左上角 “main ▼” 下拉菜单里可以看到你新推送的分支。

---

### 🪄 速查表

| 操作     | 命令行                   | IDEA 操作    |
| ------ | --------------------- | ---------- |
| 查看当前分支 | `git branch`          | 右下角标签      |
| 创建分支   | `git checkout -b 分支名` | 新建分支       |
| 切换分支   | `git checkout 分支名`    | 点击右下角切换    |
| 推送分支   | `git push origin 分支名` | Git → Push |
| 拉取更新   | `git pull origin 分支名` | Git → Pull |

---

## 🔁 六、每日协作流程

### 每天开始前

1️⃣ 拉取最新代码：

```bash
git checkout dev
git pull origin dev
```

2️⃣ 切换回自己分支：

```bash
git checkout backend-core
```

3️⃣ 合并最新开发进度（保持更新）：

```bash
git merge dev
```

4️⃣ 继续开发。

---

### 每天结束时

```bash
git add .
git commit -m "feat: 完成xxx功能"
git push origin backend-core
```

---

### 每周汇总（Sprint结束）

* 所有人提交 PR → 合并到 `dev`
* 测试无误后，负责人：

  ```bash
  git checkout main
  git merge dev
  git tag -a v1.0 -m "Sprint 1 可演示版本"
  git push origin main --tags
  ```

---

## ⚠️ 七、冲突解决规范

当多人修改同一文件，Git 会自动标出冲突：

```
<<<<<<< HEAD
你的修改
=======
队友的修改
>>>>>>> origin/dev
```

处理方式：

1. 手动编辑文件，保留正确部分；
2. 删除冲突标记；
3. 执行：

   ```bash
   git add .
   git commit -m "fix: 解决合并冲突"
   git push
   ```

---

## 🧾 八、.gitignore 建议内容

```gitignore
# IntelliJ IDEA
.idea/
*.iml

# 构建产物
target/
out/

# 系统文件
.DS_Store
Thumbs.db

# 数据库文件
*.db

# 日志
logs/
```

---

## 💬 九、提交规范

| 类型    | 前缀    | 示例                  |
| ----- | ----- | ------------------- |
| 新功能   | feat  | feat: 增加学生加分接口      |
| 修复Bug | fix   | fix: 修复数据库连接错误      |
| 样式调整  | style | style: 修改前端表格样式     |
| 其他调整  | chore | chore: 更新.gitignore |

**注意：**

* 每次提交保持内容单一、描述清晰；
* 每天 push 一次，避免分支落后太多。

---

## 🧱 十、Sprint 进度规划建议

| Sprint   | 周期  | 目标                    | 负责人   |
| -------- | --- | --------------------- | ----- |
| Sprint 1 | 第1周 | 项目初始化 + 实体类设计 + 数据库连接 | 你     |
| Sprint 2 | 第2周 | 加分模块 + 上传材料接口         | 你 & B |
| Sprint 3 | 第3周 | 审核流程 + 公示页面           | 全体    |
| Sprint 4 | 第4周 | 测试 + 汇报展示             | 全体    |

---

## ✅ 十一、最佳实践总结

1. `main`：永远是稳定、可演示版本；
2. `dev`：集成测试分支；
3. 每人各自开发独立分支；
4. 所有合并通过 **Pull Request** 完成；
5. 每次开发前先 `git pull`；
6. 提交信息简洁明了；
7. 版本号使用 tag 记录里程碑。

---

## 📘 十二、常用命令速查表

```bash
# 初始化仓库
git init

# 克隆远程仓库
git clone <url>

# 查看分支
git branch

# 新建并切换分支
git checkout -b backend-core

# 提交修改
git add .
git commit -m "feat: 完成学生实体类"

# 推送分支
git push origin backend-core

# 拉取远程更新
git pull origin dev

# 合并分支
git merge dev
```

---

## 💡 十三、团队协作小建议

* 每周末进行一次小组代码回顾；
* 使用 GitHub Issues 跟踪任务；
* 在 Pull Request 评论区互相 code review；
* 每人维护自己的分支日志。

---

## ✨ 十四、结语

> “协作开发的核心不是代码，而是同步节奏。”
> 用好分支，就能让三人团队像一个人一样高效前进。

---
