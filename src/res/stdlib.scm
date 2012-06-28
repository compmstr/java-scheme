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
