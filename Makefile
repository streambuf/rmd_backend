front:
	ssh root@rmd "cd rmd_front && git pull"
	ssh root@rmd "cd rmd_back && git pull"
	ssh root@rmd "cd rmd_back && docker-compose up --build -d frontend"