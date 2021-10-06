package coding.json.practice.batch.jobs.process;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
@Getter
@NoArgsConstructor
public class Option {
    private NumberPath<Long> field;
    private String fieldName;

    private Long currentId;
    private Long lastId;

    private Order order;

    public Option(NumberPath<Long> field, Order order){
        String[] args = field.toString().split("\\.");
        this.field = field;
        this.fieldName = args[args.length-1];
        this.order = order;
    }

    public <T> void initKeys(JPAQuery<T> query, int page){
        if(page == 0){
            initFirstId(query);
            initLastId(query);
        }
    }


    public <T> void initFirstId(JPAQuery<T> query) {

        JPAQuery<T> clone = query.clone(); // clone - 쿼리 복사
        log.warn("원조 sql문 = " + clone.toString());
        boolean isGroupByQuery = isGroupByQuery(clone);

        if(isGroupByQuery) {
            currentId = clone
                    .select(field)
                    .orderBy(order.equals(Order.ASC) ? field.asc() : field.desc())
                    .fetchFirst();
        }else{
            currentId = clone
                    .select(order.equals(Order.ASC) ? field.min() : field.max())
                    .fetchOne();
        }

    }

    public <T> void initLastId(JPAQuery<T> query) {
        JPAQuery<T> clone = query.clone();
        boolean isGroupByQuery = isGroupByQuery(query);

        // 맨 끝 id 구하는 거니 반대로 정렬
        if(isGroupByQuery){
            lastId = clone
                    .select(field)
                    .orderBy(order.equals(Order.ASC) ? field.desc() : field.asc())
                    .fetchFirst();
        }else{
            lastId = clone
                    .select(order.equals(Order.ASC) ? field.max() : field.min())
                    .fetchOne();
        }

    }

    public <T> JPAQuery<T> createQuery(JPAQuery<T> query, int page) {
        log.info("now page = " + page);

        if(currentId == null){ // ?
            return query;
        }
        return query
                .where(whereExpression(order, field, page, currentId),
                        order.equals(Order.ASC) ? field.loe(lastId) : field.goe(lastId))
                .orderBy(order.equals(Order.ASC) ? field.asc() : field.desc());
    }

    public <T> void resetCurrentId(T item) { // 마지막 entity 받아서 id 필드 값 얻음
        try {
            Field f = item.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            currentId = (Long) f.get(item); // currentId 캐시
            log.info("now currentId = " + currentId);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Not Found or Not Access Field");
        }
    }

    private boolean isGroupByQuery(JPAQuery query) {
        return query.toString().contains("group by");
    }

    private BooleanExpression whereExpression(Order order, NumberPath<Long> id, int page, Long currentId){
        if(order.equals(Order.ASC)) {
            return page == 0 ? id.goe(currentId) : id.gt(currentId);
        }
        return page == 0 ? id.loe(currentId) : id.lt(currentId);
    }

}
