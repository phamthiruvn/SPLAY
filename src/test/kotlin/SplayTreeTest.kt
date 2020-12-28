import org.junit.jupiter.api.Test

class SplayTreeTest : AbstractSplayTreeTest() {

    override fun create(): CheckableSortedSet<Int> =
        SplayTree()

    @Test
    fun initTest() {
        doInitTest()
    }

    @Test
    fun addTest() {
        doAddTest()
    }

    @Test
    fun firstAndLastTest() {
        doFirstAndLastTest()
    }

    @Test
    fun removeTest() {
        doRemoveTest()
    }

    @Test
    fun iteratorTest() {
        doIteratorTest()
    }

    @Test
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
    }

    @Test
    fun subSetTest() {
        doSubSetTest()
    }
    @Test
    fun iteratorSubSetTest() {
        doIteratorSubSetTest()
    }

    @Test
    fun iteratorSubSetRemoveTest() {
        doIteratorRemoveSubSetTest()
    }

    @Test
    fun subSetRelationTest() {
        doSubSetRelationTest()
    }

    @Test
    fun subSetFirstAndLastTest() {
        doSubSetFirstAndLastTest()
    }

    @Test
    fun headSetTest() {
        doHeadSetTest()
    }

    @Test
    fun headSetRelationTest() {
        doHeadSetRelationTest()
    }

    @Test
    fun tailSetTest() {
        doTailSetTest()
    }

    @Test
    fun tailSetRelationTest() {
        doTailSetRelationTest()
    }

}