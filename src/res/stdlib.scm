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

(define (inc x)
  (+ x 1))
(define (dec x)
  (- x 1))

(define (fact x)
  (if (= x 0)
    1
    (* x (fact (dec x)))))

(define (range x)
  (range-recur 0 x))

(define (range-recur cur target)
  (if (= cur target)
    '()
    (cons cur (range-recur (inc cur) target))))

(define (range2 start len)
  (map
    (lambda x (+ start x))
    (range len)))

(define (map func lst)
  (if (empty? (cdr lst))
    (list (func (car lst)))
    (cons (func (car lst)) (map func (cdr lst)))))

(define (filter func lst)
  (if (empty? (cdr lst))
    (if (func (car lst))
      (list (car lst))
      '())
    (if (func (car lst))
      (cons (car lst) (filter func (cdr lst)))
      (filter func (cdr lst)))))

(define (nth lst x)
  (if (= x 0)
    (car lst)
    (nth (cdr lst) (dec x))))

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

