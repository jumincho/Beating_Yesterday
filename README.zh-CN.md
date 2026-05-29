<div align="center">

# beating-yesterday

**和昨天的自己竞争来激励自我成长的 Android 应用**

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/language-Java-007396?logo=java&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-21-blue)
[![Verify](https://github.com/jumincho/beating-yesterday/actions/workflows/verify.yml/badge.svg)](https://github.com/jumincho/beating-yesterday/actions/workflows/verify.yml)
![License](https://img.shields.io/badge/license-MIT-green)
![Year](https://img.shields.io/badge/year-2021-blue)

[한국어](./README.md) · [English](./README.md#english) · **中文**

</div>

---

## 概览

> 每天与昨天的自己竞争,以激励自我成长的 Android 应用

每天记录热量摄入和学习时间,并与昨天的记录对比来判定胜负。
卡路里得分以用户的 BMI 为基准计算,因此这不是一款单纯的减肥应用,
而是个性化的自我管理工具。

## 主要功能

- **用户档案** —— 录入姓名、性别、年龄、身高、体重后自动计算 BMI
- **饮食管理** —— 录入早/中/晚的食物,通过 韩国 식품안전나라 (Food Safety Korea) OpenAPI
  自动查询热量,并根据 BMI 区间(偏瘦/正常/超重)计算热量得分
- **学习计时器** —— 拖拽设定时/分/秒的圆形计时器及秒表
- **待办管理** —— 基于 SQLite 的 TODO 列表(支持下拉刷新)
- **胜负判定** —— 比较「昨天的热量得分 + 学习时间」与今天的记录,
  告诉你"是否战胜了昨天的自己"

## 屏幕结构

底部三标签 (Bottom Navigation):

| 标签 | 说明 |
| --- | --- |
| Diet | 饮食录入 · 体重管理 |
| Home | 用户信息 · 与昨天的胜负判定 |
| Productivity | TODO 列表 · 学习计时器 |

## 技术栈

- **语言**: Java
- **平台**: Android (minSdk 21 / targetSdk 32)
- **架构**: Fragment + ViewModel
- **存储**: SharedPreferences (用户资料、每日记录) + SQLite (TODO)
- **网络**: HttpURLConnection + 韩国 식품안전나라 OpenAPI
- **UI**: Material Components, ConstraintLayout, RecyclerView, Navigation Component

## 项目结构

```
app/src/main/
├── java/com/jumincho/beatingyesterday/
│   ├── MainActivity.java
│   ├── domain/                        # 与框架无关的纯逻辑(JVM 单元测试覆盖)
│   │   └── HealthMetrics.java         # BMI · 热量得分计算
│   ├── data/                          # 数据层
│   │   ├── FoodCalorieApi.java        # 식품안전나라 OpenAPI 客户端
│   │   ├── Note.java                  # TODO 模型
│   │   ├── NoteAdapter.java
│   │   └── NoteDatabase.java          # SQLite 辅助
│   └── ui/
│       ├── ProfileSetupActivity.java  # 首次启动时录入档案
│       ├── home/                      # 主页 (胜负判定)
│       ├── diet/                      # 饮食 / 体重管理
│       └── productivity/              # TODO + 计时器
│           ├── CircularTimerView.java # 自定义圆形计时器 View
│           └── TimerMode.java
└── res/
    ├── layout/        # 屏幕布局
    ├── navigation/    # 底部导航图
    └── ...
```

## 密钥管理

构建系统从 `local.properties` 读取 韩国 식품안전나라 OpenAPI 密钥,通过 Gradle
`buildConfigField` 注入为 `BuildConfig.FOOD_API_KEY`。源代码中不包含密钥字符串,
`local.properties` 已加入 `.gitignore`。

| `local.properties` 键 | `BuildConfig` 字段 | 使用位置 |
| --- | --- | --- |
| `FOOD_API_KEY` | `BuildConfig.FOOD_API_KEY` | 韩国 식품안전나라 (Food Safety Korea) 热量查询 |

## 构建方法

1. 从 [韩国 식품안전나라 OpenAPI](https://various.foodsafetykorea.go.kr/nutrient/) 申请 API 密钥
2. 在项目根目录的 `local.properties` 中添加:

   ```properties
   FOOD_API_KEY=申请的_密钥
   ```

3. 在 Android Studio 中打开项目 → `Run`

```bash
./gradlew assembleDebug
```

要求:
- Android Studio Bumblebee 及以上
- JDK 8 及以上
- Android SDK 32

## 演示资料

- 演示视频: <https://youtu.be/vW4CvgCHdco>
- 演讲幻灯片: [`docs/presentation.pptx`](docs/presentation.pptx)

## 屏幕截图

<img width="20%" src="https://user-images.githubusercontent.com/77545063/200374902-2da72615-5cf8-4d20-b950-f00962a1c795.png" alt="应用截图"/>

## 许可证

[MIT License](./LICENSE)
