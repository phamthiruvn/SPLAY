import java.util.*

class SplayTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: SplayNode<T>? = null
    override var size: Int = 0
        private set

    fun getRoot(): SplayNode<T>? {
        return root
    }

    /**
     * left rotate
     */
    private fun leftRotate(children: SplayNode<T>, parent: SplayNode<T>) {
        if (parent.parent != null) {
            if (parent == parent.parent!!.left) parent.parent!!.left = children else parent.parent!!.right = children
        }
        if (children.left != null) children.left!!.parent = parent
        children.parent = parent.parent
        parent.parent = children
        parent.right = children.left
        children.left = parent
    }

    /**
     * right rotate
     */
    private fun rightRotate(children: SplayNode<T>, parent: SplayNode<T>) {
        if (parent.parent != null) {
            if (parent == parent.parent!!.left) parent.parent!!.left = children else parent.parent!!.right = children
        }
        if (children.right != null) children.right!!.parent = parent
        children.parent = parent.parent
        parent.parent = children
        parent.left = children.right
        children.right = parent
    }

    /**
     *  splay
     */
    private fun splay(node: SplayNode<T>) {
        while (node.parent != null) {
            val parent = node.parent!!
            if (parent.parent == null) {
                if (node == parent.left) rightRotate(node, parent) else leftRotate(node, parent)
            } else {
                val grandParent = parent.parent!!
                if (node == parent.left) {
                    if (parent == grandParent.left) {
                        rightRotate(parent, grandParent)
                        rightRotate(node, parent)
                    } else {
                        rightRotate(node, node.parent!!)
                        leftRotate(node, node.parent!!)
                    }
                } else {
                    if (parent == grandParent.left) {
                        leftRotate(node, node.parent!!)
                        rightRotate(node, node.parent!!)
                    } else {
                        leftRotate(parent, grandParent)
                        leftRotate(node, parent)
                    }
                }
            }
        }
        root = node
    }


    override fun add(element: T): Boolean {
        if (findWithOutSplay(root, element) != null) return false
        var newNode = root
        var parent: SplayNode<T>? = null
        while (newNode != null) {
            parent = newNode
            newNode = if (element > parent.value) newNode.right else newNode.left
        }
        newNode = SplayNode(element)
        newNode.parent = parent
        when {
            parent == null -> root = newNode
            element > parent.value -> parent.right = newNode
            else -> parent.left =
                newNode
        }
        splay(newNode)
        size++
        return true
    }

    private fun findWithOutSplay(start: SplayNode<T>?, value: T): SplayNode<T>? {
        if (start == null) return null
        val comparison = value.compareTo(start.value)
        return if (comparison == 0) {
            start
        } else if (comparison < 0) {
            if (start.left == null) null else findWithOutSplay(start.left, value)
        } else {
            if (start.right == null) null else findWithOutSplay(start.right, value)
        }
    }

    override fun remove(element: T): Boolean {
        val node = findWithOutSplay(root, element)
        return if (node != null && node.value == element) {
            remove(node)
            size--
            true
        } else false
    }

    private fun remove(node: SplayNode<T>) {
        splay(node)
        if (node.left != null && node.right != null) {
            val pre = predecessor(node)
            pre.right = node.right
            node.right!!.parent = pre
            node.left!!.parent = null
            root = node.left
        } else if (node.right != null) {
            node.right!!.parent = null
            root = node.right
        } else if (node.left != null) {
            node.left!!.parent = null
            root = node.left
        } else {
            root = null
        }
        node.parent = null
        node.left = null
        node.right = null
    }

    private fun predecessor(start: SplayNode<T>): SplayNode<T> {
        var node = start
        node = node.left!!
        while (node.right != null) {
            node = node.right!!
        }
        return node
    }

    override fun comparator(): Comparator<in T>? = null

    override fun first(): T {
        if (root == null) throw NoSuchElementException()
        var current = root
        while (current!!.left != null) {
            current = current.left
        }
        return current.value
    }

    override fun last(): T {
        if (root == null) throw NoSuchElementException()
        var current = root
        while (current!!.right != null) {
            current = current.right
        }
        return current.value
    }


    override fun iterator(): MutableIterator<T> {
        return SplayTreeIterator()
    }

    inner class SplayTreeIterator : MutableIterator<T> {
        private var current: SplayNode<T>? = null
        private val stack = Stack<SplayNode<T>>()

        init {
            current = root
            while (current != null) {
                stack.push(current)
                current = current!!.left
            }
        }

        override fun hasNext(): Boolean = stack.isNotEmpty()

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            var node = stack.pop()
            current = node
            if (node.right != null) {
                node = node.right
                pushAll(node)
            }
            return current!!.value
        }

        private fun pushAll(node: SplayNode<T>) {
            if (node !in stack) {
                stack.push(node)
                if (node.left != null)
                    pushAll(node.left!!)
            }
        }

        override fun remove() {
            if (current == null) throw IllegalStateException()
            remove(current!!.value)
            current = null
        }
    }

    private class SubSet<T : Comparable<T>>(
        private val tree: SplayTree<T>,
        private val fromElement: T?,
        private val toElement: T?
    ) : TreeSet<T>() {

        override val size: Int
            get() {
                var size = 0
                for (aTree in tree) {
                    if (inRange(aTree)) size++
                }
                return size
            }

        override fun contains(element: T): Boolean {
            return inRange(element) && tree.contains(element)
        }

        override fun add(element: T): Boolean {
            require(inRange(element))
            return tree.add(element)
        }

        override fun remove(element: T): Boolean {
            require(inRange(element))
            return tree.remove(element)
        }

        private fun inRange(element: T): Boolean {
            return if (fromElement != null && toElement != null) element >= fromElement && element < toElement
            else if (fromElement != null) element >= fromElement
            else element < toElement!!
        }

        override fun comparator(): Comparator<in T>? = null

        override fun first(): T? {
            if (size == 0) throw NoSuchElementException()
            return if (fromElement == null) tree.first() else {
                val tree = tree.iterator()
                var t: T? = null
                var next: T?
                while (tree.hasNext()) {
                    next = tree.next()
                    t = next
                    if (next >= fromElement) break
                }
                t
            }
        }

        override fun last(): T? {
            if (size == 0) throw NoSuchElementException()
            return if (toElement == null) tree.last() else {
                val tree = tree.iterator()
                var next: T?
                var t: T? = null
                while (tree.hasNext()) {
                    next = tree.next()
                    if (next >= toElement) break
                    t = next
                }
                t
            }
        }

    }

    override fun headSet(toElement: T): SortedSet<T> {
        return SubSet(this, null, toElement)
    }

    override fun tailSet(fromElement: T): SortedSet<T> {
        return SubSet(this, fromElement, null)
    }

    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        require(fromElement <= toElement)
        return SubSet(this, fromElement, toElement)
    }

    override fun height(): Int =
        height(root)

    private fun height(node: SplayNode<T>?): Int {
        if (node == null) return 0
        return 1 + kotlin.math.max(height(node.left), height(node.right))
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: SplayNode<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

}