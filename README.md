# websocketj
redirect the STDIN/STDOUT from server side to web browser. you can operate/monitor the server side program in browser.
https://www.cnblogs.com/xiaozhi_5638/p/8760351.html

## details

the websocketj can hook other process's `STDIN` and `STDOUT`, any message received from remote websocket client will be wrriten into process's `STDIN`, and any output from process's `STDOUT` will be sent to remote websocket client.

the websocketj is written by Java, can run on `Windows(tested on Windows 10)`, `Linux(tested on CentOS 6.5)` and other platforms. the websocketj can work fine with any processes as long as the processes surpport STDIN and STDOUT, so it can be developed by `Java`, `Go`, `Python`, `C#`, `Lua`, `Ruby`, `Scala` and any other languages, all depends on you!

websocketj also surpports static web server, you can visit static resources such as html, images, js and css from it, just give a listen port as startup parameter(--ssport)!


## how to use

- Step1: develop your serverside program, MUST surpport stdin/stdout. you can use any language on any platform, java, go, C#, C++ and etc, depends on yourself.
- Step2: develop your frontend program(html), CAN receive data from backend and send data to it with WebSocket technology.
- Step3: put your html(css/js/image) static resources into a directory on the server, say `/usr/local/websocketj/html/`.
- Step4: start the websocketj with parameters, say `websocketj --wsport=8081 --ssport=8082 --staticDir=/usr/local/websocketj/html/ yourprogram args...`.
- Step5: visit the html page via any web browser that surpports WebSocket protocal. Now you can operate the serverside program with your local browser!

**startup paramters**

- `--wsport`: websocket server port.

- `--ssport`: static web server port.

- `--staticDir`: the static resource directory.


>after the websocketj started successfully, input `http://ip:ssport/ (http://192.168.3.74:8082/)` in your browser location bar, you will see the welcome page!


## demo
waiting for update...


