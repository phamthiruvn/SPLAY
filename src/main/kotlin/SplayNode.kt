class SplayNode<T> constructor(
    var value: T
) {
    var parent: SplayNode<T>? = null
    var right: SplayNode<T>? = null
    var left: SplayNode<T>? = null
}