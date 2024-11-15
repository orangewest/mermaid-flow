
```mermaid

flowchart LR

	start[开始]
	10000{ }
	11000[y=y+1]
    12000[y=y+2]
	
	start-->10000--"x>y"-->11000
    10000--"x<=y"-->12000
    
```