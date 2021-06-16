# Setsuna README.MD

Setsuna网络库，基于okhttp封装，提供基础RESTFul接口请求能力，有任何问题或建议都可以提issue

# 添加依赖至项目

### 在根目录下`build.gradle`文件下添加如下来源

```Groovy
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
```


### 添加依赖

```Groovy
dependencies {
          implementation 'com.github.jailbreakme3:Setsuna:x.x.x'
  }

```


# 使用范例

### 初始化

```Kotlin
Setsuna.init {
   // define host (optional)
   baseUrl("https://www.baidu.com/")
     // customized okHttpClient(optional, when empty, use default client)
    okHttpClient {
        OkHttpClient()
    }
    converter {
        // conventer which implements ResponseBodyConverter (optional)
        GsonConverter.create()
    }
}
```


### 网络请求

本库提供了两种api方案

#### 常规api方式

```Kotlin
SetsunaCoroutineScope.launch {
    // run in a new thread or corountine
    val responseData = get {
        url = "xxxxxxxx"
        param {
            "content" - "测试内容"
            "type" - "0"
        }
        // optional grammar
        param(mapOf("key" to "value"))
        param("key" to "value")
        header {
          ”app_id" - "xxxxxx"
           "app_secret" - "xxxxxxxxx"
        }
        header(APP_ID, APP_SECRET)
    }.parseOrThrow<ResultBean>().apply {
        this.log()
    }
}
```


#### DSL方式

```Kotlin
SetsunaCoroutineScope.launch {
  // run in a new thread or corountine
  Setsuna.get {
        url = ""
        params {
            "content" - "测试内容"
            "type" - "0"
        }
        headers {
            "key" - "value"
        }

        onSuccess {
            // callback when request succeed
            // handle the response yourself
            it.body()
        }

        onSuccessWithParse<QRCodeData> {
            // callback when request succeed
            // get the response with a parsed nullable entity
        }

        onStart {
            // callback when request start
        }

        onFinish {
            // callback when request finished
        }

        onFail {
            // callback when request failed
        }
    }
}
```


