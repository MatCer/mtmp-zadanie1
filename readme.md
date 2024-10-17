Copy env example with command below and fill your API_URL
```
cp app/src/main/assets/env.example app/src/main/assets/env
```
As a server I used simple python flask:
run to start:
```
python server.py
```

Android 9+ hate http so I also used ngrok to create fast https tunnel
```
ngrok http http://localhost:5000 
```