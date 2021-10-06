package coding.json.training.repository.member;

import coding.json.training.domain.Member;
import coding.json.training.domain.dept.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static coding.json.training.domain.QMember.member;
import static coding.json.training.domain.dept.QDepartment.department;
import static coding.json.training.domain.dept.QPostAdmin.postAdmin;

@Repository
@RequiredArgsConstructor
public class MemberQuerydslRepository { // extends QuerydslRepositorySupport

    private final JPAQueryFactory queryFactory;

    public List<Member> findByName(String name){
        return queryFactory
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetch(); // fetchOne() fetchCount()
        // .orderBy(member.id.desc())
        // groupBy having join
        // select avg sum max min
        // paging >> offset, limit


    }

    public List<Member> findPostAdmins(Category category){

        /*
        queryFactory.select(Projections.constructor(PostAdminDto.class, member.id, member.name, postAdmin.category))
                .from(member)
                .innerJoin(postAdmin) // innerJoin 교집합 출력, leftJoin은 상대 데이터가 null이더라도 자기 데이터 모두 출력
                .on(member.department.id.eq(postAdmin.id))
                .where(postAdmin.category.eq(category))
                .fetch();
        */
        return queryFactory.select(member)
                .from(department)
                .join(department.members, member)
                .fetchJoin()
                .join(postAdmin)
                .on(department.id.eq(postAdmin.id)) // fetchJoin 하기?
                .where(postAdmin.category.eq(category))
                .fetch();
    }

    /*
    0. fetchJoin
    List<Member> fetch = queryFactory.selectFrom(member).innerJoin(member.department).fetchJoin().fetch();

    1.
    List<Department> departments = queryFactory.selectFrom(department) // department.* member.* 조회됨(department.member로 join해야함)
            .leftJoin(department.members, member) // , 다음은 alias
            .fetchJoin()
            .fetch(); // 이후에 원하는 dto로 변형

    2.
    Map<Department, List<Member>> transForm = queryFactory.from(department)
            .leftJoin(department.members, member)
            .transform(groupBy(department).as(list(member))); // 이후에 원하는 dto로 변형

            // .as(new Dto(department.col, member.col)) 생성자에 @QueryProjection
            // .as(map(col, col)) .asMultimap(map(col, col))
            // .as(sum()) .as(max(()) .as(Projections.constructor())



    // oneToOne 관계도 cross join과 추가 쿼리 피하기 위해 innerJoin 필요



    #. group by 사용 시
    // index 컬럼으로 group by하면 정렬 없이 정렬,
    // index 아닌 컬럼으로 group by 하는 경우는 order by null asc 사용해서 정렬 없이 정렬
    ##. querydsl에서는 새로 생성
    // public class OrderByNull extends OrderSpecifier {
    //     public static final OrderByNull DEFAULT = new OrderByNull();
    //
    //     private OrderByNull() {
    //         super(Order.ASC, NullExpression.DEFAULT, NullHandling.Default);
    //     }
    // }


    count 보다 exists의 성능이 더 좋음 >> exists 없기에 fetchFirst()으로 우회해서 사용(== limit 1)


    동적 쿼리 where절에 BooleanExpression return null or other

    서브 쿼리
    - select 절 ExpressionUtils.as()
    queryFactory
                .select(Projections.fields(StudentCount.class,
                        academy.name.as("academyName"),

                        ExpressionUtils.as(
                                JPAExpressions.select(count(student.id))
                                        .from(student)
                                        .where(student.academy.eq(academy)),
                                "studentCount")
                )).from()

     - where 절은 ExpressionUtils 없이 가능
     .where(academy.id.in(
            JPAExpressions
                    .select(student.academy.id)
                    .from(student)
                    .where(student.id.eq(studentId))))

     */
}