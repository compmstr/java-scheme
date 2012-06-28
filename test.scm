(define (factorial n)
  (define (iter product counter max-count)
    (if (> counter max-count)
      product
      (iter (+ counter product)
            (+ counter 1)
            max-count)))
  (iter 1 1 n))

(print "(factorial 5): " (factorial 5) #\newline)

(define (fact x)
  (if (= x 0)
    1
    (* x (fact (- x 1)))))
(print "(fact 5): " (fact 5) #\newline)

(define (range-recur cur target)
  (if (= cur target)
    (list target)
    (cons cur (range-recur (+ cur 1) target))))

(define (range x)
  (range-recur 0 x))

(print "(range 10): " (range 10) #\newline)

(define (map func lst)
  (if (eq? (cdr lst) '())
    (list (func (car lst)))
    (cons (func (car lst)) (map func (cdr lst)))))
