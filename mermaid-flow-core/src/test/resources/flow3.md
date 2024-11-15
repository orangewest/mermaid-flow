
```mermaid

flowchart LR

	start[开始]
	10000{x>y}
	11000[y=y+1]
    12000[y=y+2]
	
	start-->10000--true-->11000
    10000--false-->12000
    
```