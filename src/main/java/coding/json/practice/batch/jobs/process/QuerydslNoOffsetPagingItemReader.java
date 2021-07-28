package coding.json.practice.batch.jobs.process;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import javax.persistence.EntityManagerFactory;
import java.util.function.Function;

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
        // super(entityManagerFactory, pageSize, queryFunction);
        this();
        super.entityManagerFactory = entityManagerFactory;
        super.queryFunction = queryFunction;
        setPageSize(pageSize);

        this.options = options;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {

        clearIfTransacted();

        JPAQuery<T> query = createQuery().limit(getPageSize()); //

        initResults();

        fetchQuery(query);

        resetCurrentIdIfNotLastPage(); // 조회된 페이지의 마지막 ID 캐시
    }

    @Override
    protected JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        options.initKeys(queryFunction.apply(queryFactory), getPage()); // 제일 첫번째 페이징시 시작해야할 ID 찾기

        return options.createQuery(queryFunction.apply(queryFactory), getPage()); // 캐시된 ID를 기준으로 페이징 쿼리 생성
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
