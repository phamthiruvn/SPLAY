import java.util.*

class SplayTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    class SplayNode<T> constructor(
        var value: T
    ) {
        var parent: SplayNode<T>? = null
        var right: SplayNode<T>? = null
        var left: SplayNode<T>? = null
    }

    var root: SplayNode<T>? = null
        private set

    override var size: Int = 0
        private set


    /**
     * left rotate
     */
    private fun leftRotate(children: SplayNode<T>, parent: SplayNode<T>) {
        val grandParent = parent.parent
        if (grandParent != null) {
            if (parent == grandParent.left) grandParent.left = children else grandParent.right = children
        }
        val childrenLeft = children.left
        if (childrenLeft != null) childrenLeft.parent = parent
        children.parent = grandParent
        parent.parent = children
        parent.right = childrenLeft
        children.left = parent
    }

    /**
     * right rotate
     */
    private fun rightRotate(children: SplayNode<T>, parent: SplayNode<T>) {
        val grandParent = parent.parent
        if (grandParent != null) {
            if (parent == grandParent.left) grandParent.left = children else grandParent.right = children
        }
        val childrenRight = children.right
        if (childrenRight != null) childrenRight.parent = parent
        children.parent = grandParent
        parent.parent = children
        parent.left = childrenRight
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

    override fun contains(element: T): Boolean {
        val node = findWithOutSplay(root, element)
        if (node != null) splay(node)
        return node != null
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
        return when {
            comparison == 0 -> {
                start
            }
            comparison < 0 -> findWithOutSplay(start.left, value)
            else -> findWithOutSplay(start.right, value)
        }
    }

    override fun remove(element: T): Boolean {
        val node = findWithOutSplay(root, element)
        return if (node != null) {
            remove(node)
            size--
            true
        } else false
    }

    private fun remove(node: SplayNode<T>) {
        splay(node)
        val nodeLeft = node.left
        val nodeRight = node.right
        if (nodeLeft != null && nodeRight != null) {
            val pre = predecessor(node)
            pre.right = node.right
            nodeRight.parent = pre
            nodeLeft.parent = null
            root = node.left
            splay(pre)
        } else if (nodeRight != null) {
            nodeRight.parent = null
            root = node.right
        } else if (nodeLeft != null) {
            nodeLeft.parent = null
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

    override fun clear() {
        root = null
        size = 0
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

        override fun hasNext(): Boolean {
            return stack.isNotEmpty()
        }

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

    inner class SubSet(
        private val fromElement: T?,
        private val toElement: T?
    ) : SortedSet<T> {

        private val tree = this@SplayTree

        override var size: Int = 0
            private set
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

        override fun first(): T {
            return iterator().asSequence().first()
        }

        override fun last(): T {
            return iterator().asSequence().last()
        }

        override fun addAll(elements: Collection<T>): Boolean {
            var added = false
            for (element in elements) {
                if (!tree.contains(element) || inRange(element)) added = this.add(element)
            }
            return added
        }

        override fun removeAll(elements: Collection<T>): Boolean {
            var removed = false
            for (element in elements) {
                if (tree.contains(element)) removed = this.remove(element)
            }
            return removed
        }

        override fun clear() {
            val elements = this.toList()
            this.removeAll(elements)
        }

        override fun retainAll(elements: Collection<T>): Boolean {
            var removed = false
            for (element in tree) {
                if (!elements.contains(element)) removed = this.remove(element)
            }
            return removed
        }

        override fun containsAll(elements: Collection<T>): Boolean {
            var contains = true
            for (element in elements) {
                if (!tree.contains(element)) contains = false
            }
            return contains
        }

        override fun isEmpty(): Boolean {
            return size == 0
        }

        override fun iterator(): MutableIterator<T> {
            return SubSetIterator()
        }

        inner class SubSetIterator internal constructor() : MutableIterator<T> {
            private val treeIterator = tree.iterator()
            private var next: T? = null
            private var current: T? = null

            private fun findNext() {
                next = if (treeIterator.hasNext()) treeIterator.next() else null
                while (next != null && !inRange(next!!) && treeIterator.hasNext()) {
                    next = treeIterator.next()
                }
                if (next != null && !inRange(next!!)) next = null
            }

            init {
                findNext()
            }

            override fun hasNext(): Boolean {
                return next != null
            }

            override fun next(): T {
                if (!hasNext()) throw NoSuchElementException()
                current = next
                findNext()
                return current!!
            }

            override fun remove() {
                if (current == null) throw IllegalStateException()
                remove(current)
                current = null
            }
        }

        override fun subSet(p0: T, p1: T): SortedSet<T> {
            require(p0 <= p1 && inRange(p0) && inRange(p1))
            return SubSet(p0, p1)
        }

        override fun headSet(p0: T): SortedSet<T> {
            require(inRange(p0))
            return SubSet(null, p0)
        }

        override fun tailSet(p0: T): SortedSet<T> {
            require(inRange(p0))
            return SubSet(p0, null)
        }

    }

    override fun headSet(toElement: T): SortedSet<T> {
        return SubSet(null, toElement)
    }

    override fun tailSet(fromElement: T): SortedSet<T> {
        return SubSet(fromElement, null)
    }

    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        require(fromElement <= toElement)
        return SubSet(fromElement, toElement)
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