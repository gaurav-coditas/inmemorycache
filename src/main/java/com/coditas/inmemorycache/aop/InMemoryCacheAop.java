package com.coditas.inmemorycache.aop;

import com.coditas.inmemorycache.model.Employee;
import com.coditas.inmemorycache.util.InMemoryCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * AOP class to intercept DB calls and handle caching mechanism
 * InMemoryCache class is used to store cache
 */
@Aspect
@Component
public class InMemoryCacheAop {

    /** reference to cache object. */
    private InMemoryCache inMemoryCache = InMemoryCache.getInstance();

    /**
     * get by id point cut.
     * @param id id
     */
    @Pointcut("execution(* com.coditas.inmemorycache.repository.EmployeeRepository.findById(..)) && args(id,..)")
    public void getId(Long id) {
        // get by id point cut
    }

    /**
     * save point cut.
     * @param employee employee
     */
    @Pointcut("execution(* com.coditas.inmemorycache.repository.EmployeeRepository.save(..)) && args(employee,..)")
    public void save(Employee employee) {
        // save point cut
    }

    /**
     * delete by id point cut.
     * @param id id
     */
    @Pointcut("execution(* com.coditas.inmemorycache.repository.EmployeeRepository.deleteById(..)) && args(id,..)")
    public void delete(Long id) {
        // delete by id point cut
    }

    /**
     *
     * @param joinPoint joinpoint
     * @param id id
     * @return returns employee by id
     * @throws Throwable Throwable
     */
    @Around(value = "getId(id)", argNames = "joinPoint,id")
    public Object around(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        Object obj = inMemoryCache.get(String.valueOf(id));
        if (obj != null) {
            return obj;
        }
        obj = joinPoint.proceed();
        inMemoryCache.put(String.valueOf(id), obj);
        return obj;
    }

    /**
     *
     * @param joinPoint joinPoint
     * @param employee employee
     * @return returns saved employee
     * @throws Throwable Throwable
     */
    @Around(value = "save(employee)", argNames = "joinPoint,employee")
    public Object aroundSave(ProceedingJoinPoint joinPoint, Employee employee) throws Throwable {
        Object obj = joinPoint.proceed(new Object[]{employee});
        inMemoryCache.put(String.valueOf(((Employee) obj).getId()), obj);
        return obj;
    }

    /**
     *
     * @param joinPoint joinPoint
     * @param id employee id
     * @throws Throwable Throwable
     */
    @Around(value = "delete(id)", argNames = "joinPoint,id")
    public void aroundDelete(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        inMemoryCache.delete(String.valueOf(id));
        joinPoint.proceed();
    }

}
