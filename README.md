# PerformanceBenchmarking
Performance benchmarking for thrift based server

Overview :
Performance testing or benchmarking is technically challenging area to explore. It involves lot of variable parameters so its easy to get confused

Objectives :

Finding the QPS
Finding the application issue wrt synchronous blocks
Experimenting with variables ( Example : number of threads , queue size etc )

How to use it ?
1. Start MyServer
2. Invoke RequestMaker

How QPS is control :
- Firing the request with same QPS for 2 minutes and then increase the QPS
- To start with requests will be fired with 500 QPS
- Currently we are allowing max QPS of 2500 per host


Overview :
 In idealist scenario , how many threads to instantiate ?
   - Execute command lscpu to find out the cpu details
   - Number of threads = Sockets * Core(s) per socket * Thread(s) per core
   - Only one thread can run on one core
   - We can experiment with number of threads to find out the optimal solution
 How many TCP connections can be established from client to server ?
   - Command to find out the available ports : sysctl net.ipv4.ip_local_port_range ( available ports can be calculated usiong this )
   - Command to find out tcp connection timeout : sysctl net.ipv4.tcp_fin_timeout ( using this we can find out the number of connections per second )
   - Number of connection/second = available ports * number of connections per second

 How to send 3000 requests per second from single machine ?
   This can be achieved by establishing the connection asynchronously. Release the thread after establishing tcp connection with server and register for callback



