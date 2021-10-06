package coding.json.practice.batch.jobs.process;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Function;

@Slf4j
public class QuerydslNoOffsetPagingItemReader<T> extends QuerydslPagingItemReader<T> {

    private Option options;

    private QuerydslNoOffsetPagingItemReader() {
        super();
        setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
    }

    public QuerydslNoOffsetPagingItemReader(EntityManagerFactory entityManagerFactory,
                                            int pageSize,
                                            Option options, // 조건식
                                            Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
        super(entityManagerFactory, pageSize, queryFunction); // pageSize == chunkSize
        this.options = options;
        setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {

        EntityTransaction tx = getTxOrNull();

        JPAQuery<T> query = createQuery().limit(getPageSize()); // option에서 가공된 쿼리에 여기서 페이징
        log.info("여기는 noOffset 리더입니다. chunckSize(pageSize) = " + getPageSize());

        initResults();

        fetchQuery(query, tx);

        resetCurrentIdIfNotLastPage(); // 조회된 페이지의 마지막 ID 캐시(갱신)
    }

    @Override
    protected JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<T> query = queryFunction.apply(queryFactory);
        options.initKeys(query, getPage()); // 제일 첫번째 페이징시 시작해야할 ID 찾기

        return options.createQuery(query, getPage()); // 캐시된 ID를 기준으로 페이징 쿼리 생성
    }

    private void resetCurrentIdIfNotLastPage() {
        if (isNotEmptyResults()) {
            options.resetCurrentId(getLastItem());
        }
    }

    // 조회결과가 Empty이면 results에 null이 담긴다
    private boolean isNotEmptyResults() {
        return !CollectionUtils.isEmpty(results) && results.get(0) != null;
    }

    private T getLastItem() {
        return results.get(results.size() - 1);
    }
}
