;Problema 1

;a


(defun insereazaPar (l el poz)
	(cond
		((null l) nil)
		((equal (mod poz 2) 0) (cons (car l) (cons el (insereazaPar (cdr l) el (+ 1 poz) ) ) ))
		(t (cons (car l) (insereazaPar (cdr l) el (+ 1 poz) ) ) )
	)
)

(defun main1a (l el)
	(insereazaPar l el 1)
)

;b

(defun inverseaza (l)
	(cond
		((null l) nil)
		( (listp (car l)) (append (inverseaza (cdr l)) (inverseaza (car l))))
		( t (append (inverseaza (cdr l)) (list (car l))) )
	)
)   

;c

(defun cmmdc (a b)
	(cond
		((not (numberp a)) b)
		((not (numberp b)) a)
		((= a 0) b)
		((= b 0) a)
		((> a b) (cmmdc b (mod a b)))
		(t (cmmdc a (mod b a)))
	)
)

(defun main1c (l)
	(cond
		((null l) nil)
		((not (numberp (car l))) (main1c (cdr l)))
		((listp (car l)) (cmmdc (main1c (car l)) (main1c (cdr l))))
		(t (cmmdc (car l) (main1c (cdr l))))
	)
)

;d

(defun aparitii(l el nr)
	(cond
		((null l) nr)
		((listp (car l)) (aparitii (car l) el (aparitii (cdr l) el nr)))
		((equal (car l) el) (aparitii (cdr l) el (+ 1 nr)))
		(t (aparitii (cdr l) el nr))
	)
)

(defun main1d (l el)
	(aparitii l el 0)
)




;Problema 2

;a

(defun elementN (l poz nr)
	(cond
		((null l) nil)
		((equal poz nr) (car l))
		(t (elementN (cdr l) (+ 1 poz) nr))
	)
)

(defun main2a (l nr)
	(elementN l 1 nr)
)

;b

(defun exista (l el)
	(cond
		((null l) nil)
		((listp (car l)) (or (exista (car l) el) (exista (cdr l) el)))
		((equal (car l) el) t)
		(t (exista (cdr l) el))
	)
)


;c

(defun liste (l rez)
  (cond
    ((null l) rez) 
    ((listp (car l)) (liste (cdr l)(list rez (car l))))
    (t (liste (cdr l) rez))
  )
)

(defun main2c (l)
  (liste l l)
)

;d

(defun multime (l)
	(cond
		((null l) nil)
		((exista (cdr l) (car l)) (multime (cdr l)))
		(t (cons (car l) (multime (cdr l))))
	)
)




;Problema 3

;a

(defun produsVectori(l1 l2)
	(cond
		((null l1) 0)
		((null l2) 0)
		(t (+ (* (car l1)(car l2) )(produsVectori (cdr l1)(cdr l2) ) ))
	)
)

;b

(defun maxim (a b)
	(cond
		((null a) b)
		((null b) a)
		((> a b) a)
		(t b)
	)
)

(defun adancime (l nr)
	(cond
		((null l) nr)
		((listp (car l)) (maxim (adancime (car l)(+ 1 nr) )(adancime (cdr l) nr) ))
		(t (adancime (cdr l) nr))
	)
)

(defun main3b (l)
	(adancime l 1)
)

;c

(defun minim (l min)
	(cond
		((null l) min)
		((< (car l) min) (minim (cdr l) (car l)))
		(t (minim (cdr l) min))
	)
)

(defun main_min(l)
    (minim l (car l))
)

(defun sortare (l rez)
  (cond
    ((null l) rez)
    ((= (car l) (main_min (cdr l))) (sortare (cdr l) (list (car l) rez)))
    (t (sortare (cdr l) rez))
  )
)


(defun main3c (l)
	(sortare (multime l) nil) 
)

;d

(defun intersectie (l1 l2 rez)
	(cond
		((or (null l1) (null l2)) rez)
		((exista l2 (car l1)) (intersectie (cdr l1) l2 (append rez (list (car l1)))) )
		(t (intersectie (cdr l1) l2 rez))
	)
)

(defun main3d (l1 l2)
	(intersectie l1 l2 nil)
)




;Problema 8

;b

(defun inversare (l rez)
	(cond
		((null l) rez)
		(t (inversare (cdr l) (cons (car l) rez)))
	)
)

(defun mainInv (l)
	(inversare l nil)
)

;(defun succesor (l contor rez)
;	((and (> contor 0) (null l)) (list rez (list contor)))
;	((and (= contor 0) (null l)) rez)
;	(t (succesor (cdr l) (mod (+ (car l) contor) 10)  ))
;)













                                                                                