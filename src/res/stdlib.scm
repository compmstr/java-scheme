(define (caar obj) (car (car obj)))
(define (cadr obj) (car (cdr obj)))
(define (cddr obj) (cdr (cdr obj)))
(define (cdar obj) (cdr (car obj)))
(define (caaar obj) (car (car (car obj))))
(define (caadr obj) (car (car (cdr obj))))
(define (cadar obj) (car (cdr (car obj))))
(define (caddr obj) (car (cdr (cdr obj))))
(define (cdddr obj) (cdr (cdr (cdr obj))))
(define (cdaar obj) (cdr (car (car obj))))
(define (cddar obj) (cdr (cdr (car obj))))
(define (cdadr obj) (cdr (car (cdr obj))))

(define (inc x)
  (+ x 1))
(define (dec x)
  (- x 1))

(define (fact x)
  (if (= x 0)
    1
    (* x (fact (dec x)))))

;(define (range x)
  ;(range-recur 0 x))

;(define (range-recur cur target)
  ;(if (= cur target)
    ;'()
    ;(cons cur (range-recur (inc cur) target))))

;(define (range2 start len)
  ;(map
    ;(lambda x (+ start x))
    ;(range len)))

(define (range end)
  (let ((accum '())
        (cur (dec end)))
    (while (>= cur 0)
      (set! accum (cons cur accum))
      (set! cur (dec cur)))
    accum))

(define (range2 start count)
  (let ((end (dec (+ start count))))
    (let ((accum '())
          (cur end))
      (while (>= cur start)
        (set! accum (cons cur accum))
        (set! cur (dec cur)))
      accum)))

(define (count lst)
  (let ((i 0))
    (while (not (empty? lst))
      (set! i (inc i))
      (set! lst (cdr lst))
    i)))

(define (map func lst)
  (if (empty? (cdr lst))
    (list (func (car lst)))
    (cons (func (car lst)) (map func (cdr lst)))))

(define (map-i func lst)
  (let ((accum (vector)))
    (while (not (empty? lst))
      (vector-add! accum (func (car lst)))
      (set! lst (cdr lst))
    (vector->list accum))))

(define (filter func lst)
  (if (empty? (cdr lst))
    (if (func (car lst))
      (list (car lst))
      '())
    (if (func (car lst))
      (cons (car lst) (filter func (cdr lst)))
      (filter func (cdr lst)))))

(define (vector-filter func vec)
  (let ((size (vector-length vec))
        (i 0)
        (accum (vector)))
    (while (< i size)
      (if (func (vector-ref vec i))
        (vector-add! accum (vector-ref vec i)))
      (set! i (inc i)))
    accum))

(define (filter-i func lst)
  (let ((accum (vector)))
    (while (not (empty? lst))
      (let ((item (car lst)))
        (if (func item)
          (vector-add! accum item)))
      (set! lst (cdr lst)))
    accum))

;(define (nth lst x)
  ;(if (= x 0)
    ;(car lst)
    ;(nth (cdr lst) (dec x))))

(define (nth lst x)
  (if (< x 0)
    '()
    (let ((i 0))
      (while (< i x)
        (set! i (inc i))
        (set! lst (cdr lst)))
      (car lst))))
      
(define (not x)
  (if x #f #t))

(define (even? x)
  (= (remainder x 2) 0))

(define (odd? x)
  (not (even? x)))

(define (myconcat lst item)
  (if (empty? lst)
    item
    (cons (car lst) 
          (myconcat (cdr lst) item))))

(define (empty? lst)
  (eq? lst '()))

(define (pow n exp)
  (if (= exp 2)
    (* n n)
    (if (< exp 2)
      1
      (* n (pow n (dec exp))))))

(define (pow-i n exp)
  (let ((accum n)
        (i (dec exp)))
    (while (> i 0)
      (set! accum (* accum n))
      (set! i (dec i)))
    accum))

;Lets you know if a list, vector, or hashmap contains an item
; with a hashmap, it looks for a key, not a value
(define (contains? lst item)
  (let ((retval #f))
    (cond 
      ((list? lst)
        (while (not (empty? lst))
          (if (eq? (car lst) item)
            (begin
              (set! retval #t)
              ;Break out of the loop
              (set! lst '()))
            (set! lst (cdr lst)))))
      ((vector? lst)
        (let ((size (vector-length lst))
              (i 0))
          (while (< i size)
            (if (eq? (vector-ref lst i) item)
              (begin
                (set! retval #t)
                ;Break out of the loop
                (set! i (inc size)))
              (set! i (inc i))))))
        ((hashmap? lst)
          ;Get the vector of keys, and look through it
          (set! retval (hashmap-key? lst item))))
      retval))
