# brokage
* Java version is 17
* Spring Boot version is 3.3.4

# How to run
* Project can be run by docker
```
  docker-compose up -d
```

# How to use

* postman collection can be found in repo as brokage.postman_collection
* collection contains all requests with basic examples
* all endpoints requires jwt auth as Bearer token
* to get the token use signup and login endpoints
* if you signup as customer you can only use customer labeled endpoints with that token
* if signed up as admin both endpoints can be used, however, customer endpoints will only return user related data.
* admin endpoints differ from customer endpoints by giving the user email as query param. this is already on postman collection
* in order list endpoints there is a pagination apart from date filter. page = 0 gets the first page while pageSize declares element count in one page.

# What is missing
* Request body validations
* some unit cases on basic service classes (auth service)
* bonus 2 in the case file
* better error responses and some response classes
* i was planning to implement those but i did not want to lose time since the deadline was close.