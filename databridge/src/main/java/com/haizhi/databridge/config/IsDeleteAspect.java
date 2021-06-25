package com.haizhi.databridge.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.haizhi.data.jpa.domain.IsDelete;
import com.haizhi.databridge.util.SpringUtils;

@Component
@Scope
@Aspect
@Slf4j
public class IsDeleteAspect {
    static Map<CriteriaQuery<?>, Object> criteriaQueryGenForSpringDataJpaQueryMethod = new ConcurrentHashMap();

    public IsDeleteAspect() {
    }

    @Around("execution(* javax.persistence.EntityManager.createQuery(javax.persistence.criteria.CriteriaQuery))")
    public Object createQueryx(ProceedingJoinPoint pjp) throws Throwable {
        if (isPrimary(pjp)) {
            return pjp.proceed();
        }

        CriteriaQuery<?> cq = (CriteriaQuery) pjp.getArgs()[0];
        if (criteriaQueryGenForSpringDataJpaQueryMethod.containsKey(cq)) {
            return pjp.proceed();
        } else {
            Predicate pd = cq.getRestriction();
            System.err.println(this.toString() + " " + cq.toString());
            if (!IsDelete.class.isAssignableFrom(cq.getResultType())) {
                return pjp.proceed();
            } else {
                Set<Root<?>> roots = cq.getRoots();
                Root<?> root = null;
                if (roots.isEmpty()) {
                    root = cq.from(cq.getSelection().getJavaType());
                } else {
                    root = (Root) roots.iterator().next();
                }

                EntityManager em = (EntityManager) pjp.getTarget();
                Predicate pd2 = em.getCriteriaBuilder().equal(root.get("isDel"), 0);
                cq.where(new Predicate[]{pd, pd2});
                criteriaQueryGenForSpringDataJpaQueryMethod.put(cq, "");
                return pjp.proceed();
            }
        }
    }

    @Around("execution(* javax.persistence.EntityManager.createQuery(..))")
    public Object createQuery(ProceedingJoinPoint pjp) throws Throwable {
        if (isPrimary(pjp)) {
            return pjp.proceed();
        }

        Object[] args = pjp.getArgs();
        if (args[0] instanceof String) {
            String jpql = String.valueOf(args[0]);
            if (args.length == 2) {
                Object arg2 = args[1];
                if (arg2 instanceof Class) {
                    Class<?> clazz = (Class) arg2;
                    if (!IsDelete.class.isAssignableFrom(clazz)) {
                        return pjp.proceed();
                    }
                }
            }

            this.checkDeleteClause(jpql);
            if (jpql.contains("isDel")) {
                return pjp.proceed();
            } else {
                if (jpql.toUpperCase().contains("WHERE")) {
                    args[0] = jpql + " AND isDel=0";
                } else {
                    args[0] = jpql + " WHERE isDel=0";
                }

                return pjp.proceed(args);
            }
        } else {
            return pjp.proceed();
        }
    }

    @Around("execution(* javax.persistence.EntityManager.createNativeQuery(..))")
    public Object createNativeQuery(ProceedingJoinPoint pjp) throws Throwable {
        if (isPrimary(pjp)) {
            return pjp.proceed();
        }
        Object[] args = pjp.getArgs();
        String nativeSQL = String.valueOf(args[0]);
        if (args.length == 2) {
            Object arg2 = args[1];
            if (arg2 instanceof Class) {
                Class<?> clazz = (Class) arg2;
                if (!IsDelete.class.isAssignableFrom(clazz)) {
                    return pjp.proceed();
                }
            }
        }

        this.checkDeleteClause(nativeSQL);
        if (nativeSQL.contains("is_del")) {
            return pjp.proceed();
        } else {
            if (nativeSQL.toUpperCase().contains("WHERE")) {
                args[0] = nativeSQL + " AND is_del=0";
            } else {
                args[0] = nativeSQL + " WHERE is_del=0";
            }

            return pjp.proceed(args);
        }
    }

    public void checkDeleteClause(String sql) {
        if (sql.toUpperCase().contains("DELETE")) {
            log.info(sql);
        }
    }

    private boolean isPrimary(ProceedingJoinPoint pjp) {
        return ((EntityManager) pjp.getTarget()).getEntityManagerFactory()
                == SpringUtils.getBean("entityManagerFactoryPrimary");
    }
}
