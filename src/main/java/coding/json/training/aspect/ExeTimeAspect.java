package coding.json.training.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ExeTimeAspect {

    @Pointcut("execution(* coding.json.training.service.MemberService.*(..))") // 공통기능 적용 대상 설정
    private void trainingServiceTarget(){
    }

    @Pointcut("execution(* coding.json.practice.service.NewService.*(..))")
    private void practiceServiceTarget(){
    }

    // execution
    // public void * *(..)  // method 종류, return 종류 (..)는 파라미터 0개 이상을 뜻함
    // * get*(..)       메서드 명
    // * com.abc.service.AccountService.*(..)
    // * com.abc.service.*.*(..)
    // * com.abc.service..*.*(...)
    // within, bean 등 있음
    @Around("trainingServiceTarget()") // 여기에 바로 정규식으로 대상 설정도 가능
    public Object measureTraining(ProceedingJoinPoint joinPoint) throws Throwable{

        long start = System.nanoTime(); // 대상 실행 전 공통 기능

        try{
            Object result = joinPoint.proceed(); // 위에서 설정한 대상 실행
            return result;

        }finally{ // 대상 실행 후 공통 기능

            long finish = System.nanoTime();
            Signature sig = joinPoint.getSignature();
            System.out.printf("%s.%s(%s) 실행 시간 :  %d ns\n",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    sig.getName(), Arrays.toString(joinPoint.getArgs()),
                    (finish - start));
        }

    }

    @Around("practiceServiceTarget()")
    public Object measurePractice(ProceedingJoinPoint joinPoint) throws Throwable{

        long start = System.nanoTime();

        try{
            Object result = joinPoint.proceed();
            return result;

        }finally{
            long finish = System.nanoTime();
            Signature sig = joinPoint.getSignature();
            System.out.printf("%s.%s(%s) 실행 시간 :  %d s\n",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    sig.getName(), Arrays.toString(joinPoint.getArgs()),
                    ((finish - start)) / 1000000000);
        }

    }

}
