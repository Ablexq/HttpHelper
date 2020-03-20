





# httphelper

root的app.gradle中配置：

```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```
app.gradle中配置：

```groovy
dependencies {
	implementation 'com.github.Ablexq:HttpHelper:1.0'
}

```
3. 代码中配置：
```
//添加域名配置拦截器，可直接在手机上进行切换域名
okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(new HostInterceptor())
        .build();
```

```
//添加日志拦截器，可直接在手机上进行查看日志
okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(new CaptureInfoInterceptor())
        .build();
```


# 其他

- 问题一：

![](pics/buildtype.png)

新增的build type 每个module都需要添加



- 问题二：

```

Task 'installRelease' not found in root project 'HttpHelper'. Some candidates are: 'uninstallRelease'.

```
为什么gradle命令面板没有installRelease，因为需要配置签名才能执行installRelease

解决：

![](pics/gradle.png)

只有配置了签名，release和其他build type才会有右侧的执行按钮

# 




















