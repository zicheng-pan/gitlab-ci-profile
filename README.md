# gitlab-ci-profile
## 项目主要解决两个问题：
### 1. 项目中的profile怎么指定
1. 直接在项目中使用active profile指定 <br>
```
spring:
  profiles:
    active: prod
```
启动项目
```java -jar demoforprofile-0.0.1-SNAPSHOT.jar```

2. 在启动jar包的时候指定profile<br>
取消上面的profile的配置，在启动命令中添加profile值<br>
```java -jar demoforprofile-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev```
3. 项目中build的时候指定profile的方式
```
  profiles:
    active: '@spring.profiles.active@'
```
在maven build的时候通过 ```mvn clean install -Dspring.profiles.active=dev```的方式来指定profile <br><br>
4. 在运行环境中配置环境变量 ```spring.profiles.active``` <b><font color="red">[项目中使用的这种方式，在不同的deployment-environment-variables.yaml配置文件中配置]</font></b>
<br>
可以在项目中使用代码来指定
```System.setProperty("spring.profiles.active", "dev");```
<br>
或者在环境变量中设置```export SPRING_PROFILES_ACTIVE=dev```来更改环境变量

### 操作步骤
> 项目中可以查看Dockerfile文件，里面并没有指定环境变量，但是可以在kustomize的yml配置中可以查看到container的环境变量
> 根据项目描述，我们在Dockerfile中也仅仅定义了基本信息，提供运行环境和所执行的jar包
> 执行docker build指令进行images的打包
> ```docker build . -t mytestprofileimage:001``` 然后启动容器```docker run mytestprofileimage -p 8889:8889 -d```
> 可以看到spring使用默认的profile执行。<br>
> 然后将image push到dockerhub中给k8s获取使用
> 然后定义kustomize的resource，目录放在~/kustomization目录中。
> 1. 创建k8s的命名空间<br>
> ```
> kubectl create ns demoforprofile
> ```
> 2. 创建好kustomize的配置文件后，可以查看对应的环境的k8s配置是否正确<br>
> ```
> kustomize build ./kustomization/overlays/dev
> ```
> 3. 如果检查正确，那么创建k8s集群对象<br>
> ```
> kubectl apply -k kustomization/overlays/dev
> ```
> 4. 查看生成的pods和deployment和service
> ```
> kubectl get pods -n demoforprofile #注意这里需要加上自己应用的namespace
> 
> $ kubectl get pods -n demoforprofile
> NAME                                            READY   STATUS    RESTARTS   AGE
> mytestprofileimage-deployment-c6b8db6cf-j8c6w   1/1     Running   0          45s
> #剩下的查看指令如下：
> kubectl get service -n demoforprofile #注意这里需要加上自己应用的namespace
> kubectl get deployment -n demoforprofile #注意这里需要加上自己应用的namespace
>```
> 5. 如果有错误可以进行，删除操作
> ```
> kubectl delete deployment -n demoforprofile <deployment name> #使用指定名字来删除
> kubectl delete deployment -n demoforprofile --all #删除全部
> kubectl delete pod -n demoforprofile --all
> kubectl delete  service -n demoforprofile --all
>```
> 6. 应用端口转发的方式验证pod中是否应用了相应环境的环境配置
> ```
> pod端口转发
> kubectl port-forward mytestprofileimage-deployment-c6b8db6cf-j8c6w 8889:8889 -n demoforprofile
>```
> 7. 最后可以访问本地的localhost:8889来查看环境中的值


### 2. 项目中的变量值怎么传递
在项目中的默认profile中，可以使用临时变量值。
```
testdata:
  key: ${testdatakey:unikey}
  value: ${testdatavalue:univalue}
```
在具体环境的profile中可以设置具体的值。
```aidl
testdata:
  key: devKey
  value: devValue
```
在项目中可以设置配置类```@Configuration```然后自动装载配置，可以在项目中直接使用
```aidl
    @Value("${testdata.key}")
    String key;

    @Value("${testdata.value}")
    String value;
```
