(define nummap 
  (make-hashmap 1 "one" 2 "two" 3 "three"))
(print "nummap: " nummap)
(print (hashmap-set! nummap 2 "four?"))

(define symbolmap
  (make-hashmap 'one "one" 'two "two"))
(print "symbolmap: " symbolmap)
(print (hashmap-set! symbolmap  'one "three"))

(define stringmap
  (make-hashmap "one" 'one "two" 'two))
(print "stringmap: " stringmap)
(print (hashmap-set! stringmap "one" "three"))

(define charmap
  (make-hashmap #\a "a" #\b "b"))
(print "Charmap: " charmap)
(print (hashmap-set! charmap #\a "c"))

(define (bigcalc n)
  (let ((biglist (range2 1 n)))
    (let ((bigbiglist (concat biglist biglist)))
      (filter even?
        (map (lambda x (sqrt x))
          bigbiglist)))))

(define (imp-range start len)
  (let ((end (+ start len)))
    (let ((cur end)
        (accum '()))
      (while (> cur start)
        (set! cur (dec cur))
        (set! accum (cons cur accum)))
        accum)))
        
