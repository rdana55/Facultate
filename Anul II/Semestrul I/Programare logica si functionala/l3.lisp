;Problema 6

(defun produs (x)
	(cond
		((numberp x) x)
		((atom x) 1)
		(t (apply '* (mapcar #'produs x)))
	)
)