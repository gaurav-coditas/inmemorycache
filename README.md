# inmemorycache
# InMemmoryCache.java
This class provides caching APIs, such as get, put and delete to operate on cache data
Apache commons collections *LRUMap* is used to store cached data with fixed size of 100 i.e. when cache size grows beyond 100 objects, least recently objects will be automatically discarded.
This is singleton class, so same cache object can be at application level

# InMemoryCacheAop.java
This is a utility class that intercepts database calls and handles caching of objects.
This class defines 3 point cuts for retriving, saving and deleting data and performs caching using InMemmoryCache class

# Running project
Simply clone and import maven porject, Run test class EmployeeRepositoryTest.java to execute all test cases
