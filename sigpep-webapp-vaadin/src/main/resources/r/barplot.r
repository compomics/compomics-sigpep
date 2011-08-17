x<-c()
  lines<-readLines("/Users/kennyhelsens/tmp/sigpep/AQQNHLASTNR.tsv")
	for (i in seq(length(lines))) {
		line<-lines[i]
		line.split<-strsplit(line, "\t")
		x[i]<-line.split
	}

# Calculate the max value for the x-axis
target<-x[[2]]
target<-as.double(target[3:length(target)])
x.max<-max(target)
x.max

plot(0,0,type="n", xlim=c(0,x.max), ylim=c(0,length(x)))
#plot(0,0,type="n", xlim=c(0,x.max), ylim=c(0,50))


# Plot background ions.
for (i in (3:length(x))) {
	for (j in (3:length(x[[i]]))) {
		line<-x[[i]]
		value<-as.double(line[j])
		points(x=value, y=i, col="green", cex=0.1, pch=20)
	}	
}
 
 
# Second plot target ions.
for (i in (3:length(x[[2]]))) {
  line<-x[[2]]
	value<-as.double(line[i])
	points(x=value, y=2, col="red", cex=1, pch=18)
}
 
 # First plot barcode.
for (i in (3:length(x[[1]]))) {
  line<-x[[1]]
	value<-as.double(line[i])
	abline(v=value, col="red")
}

