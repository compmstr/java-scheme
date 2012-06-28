(define (fact x)
  (if (= x 0)
    1
    (* x (fact (- x 1)))))

(define (range-recur cur target)
  (if (= cur target)
    (list target)
    (cons cur (range-recur (+ cur 1) target))))

(define (range x)
  (range-recur 0 x))

(define (map func lst)
  (if (eq? (cdr lst) '())
    (list (func (car lst)))
    (cons (func (car lst)) (map func (cdr lst)))))

(define (filter func lst)
  (if (eq? (cdr lst) '())
    (if (func (car lst))
      (list (car lst))
      '())
    (if (func (car lst))
      (cons (car lst) (filter func (cdr lst)))
      (filter func (cdr lst)))))

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

(define (nth lst x)
  (if (= x 0)
    (car lst)
    (nth (cdr lst) (- x 1))))

(define (not x)
  (if x #f #t))

(define (even? x)
  (= (remainder x 2) 0))

(define (odd? x)
  (not (even? x)))

(define (myconcat lst item)
  (if (eq? '() lst)
    item
    (cons (car lst) 
          (myconcat (cdr lst) item))))
