# PerformanceBenchmarking
Performance benchmarking for thrift based server

How to use it ?
1. Start MyServer
2. Invoke RequestMaker

How QPS is control :
- Firing the request with same QPS for 2 minutes and then increase the QPS
- To start with requests will be fired with 500 QPS
- Currently we are allowing max QPS of 2500 per host

