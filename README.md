# Go-Mirai-Client-Android
[Go-Mirai-Client](https://github.com/ProtobufBot/Go-Mirai-Client) 的安卓版

## 环境
- JDK 1.8
- Golang 1.17
- ANDROID_SDK
- gomobile

## 下载项目
1. 下载 [Go-Mirai-Client](https://github.com/ProtobufBot/Go-Mirai-Client) 源代码（后端服务）
2. 下载 [pbbot-react-ui](https://github.com/ProtobufBot/pbbot-react-ui/releases) Release，解压后放在 `Go-Mirai-Client/pkg/static/static`（前端UI）
3. 下载本项目（安卓套壳）

## 编译
1. 在 `Go-Mirai-Client` 目录下执行 `gomobile bind -target=android ./service/gmc_android`，得到 `gmc_android.aar` 和 `gmc_android-sources.jar`
2. 复制 `gmc_android.aar` 和 `gmc_android-sources.jar` 到本项目 `app/libs` 目录下
3. 编译安卓

