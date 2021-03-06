#load libs
library("ggplot2")

#input file
file <- "/Users/kennyhelsens/tmp/sigpep/metaex/LGKLYWLVTQNVDALHTK.tsv"
output <- "/Users/kennyhelsens/tmp/test.png"

#get column count of each row
st.fields <- count.fields(file, sep="\t")

#get max. column count
max.fields <- max(st.fields)

#max. fragment count equals max. column count minus the first two columns
#which countain the row_type and the ion type
max.fragment.count <- max.fields-2

#define column names
col.names <- c("row_type","ion_type", c(1:max.fragment.count))

#define column classes
colClasses <- c("character", "character", rep("numeric", max.fragment.count))

#read file
st <- read.delim(file, sep="\t", as.is=TRUE, header=FALSE, fill=TRUE, colClasses=colClasses, col.names=col.names)

#set mass accuracy
accuracy <- 1

#determine max. and min. m/z value
max.mz <- max(st[,c(3:max.fields)], na.rm=TRUE)
min.mz <- min(st[,c(3:max.fields)], na.rm=TRUE)

#get barcode, target ion series and background ion series
#row indeces
bc.rows <- which(st[,1] == "bc")
tg.rows <- which(st[,1] == "tg")
bg.rows <- which(st[,1] == "bg")

#get barcode, target ion series and background ion series
#row counts
bc.count <- length(bc.rows)
tg.count <- length(tg.rows)
bg.count <- length(bg.rows)

#get target ion spectrum
target.spectrum <- st[tg.rows, c(3:max.fields)]
rownames(target.spectrum) <- st[tg.rows, 2]

# length minus 2 leading values and 1 trailing precursor mass  
tg.ion.count<-length(which(is.na(st[2,])==FALSE))-2-1

#set barcode to plot
barcode.idx <- 1;

#get barcode
not.null <- which(st[barcode.idx,]>1)
barcode <- as.vector(st[barcode.idx,not.null])
barcode <- barcode[c(2:length(barcode))]
barcode.length <- length(barcode)

bc.number <- c()
for(i in 1:barcode.length){
  bc.number<-c(bc.number, which(target.spectrum[1,] == barcode[[i]]))
}
# Encode the barcode as an 0/1 array for the bar fill.
bc.bool<-c()
for(i in 1:tg.ion.count){
  bc.bool.curr<-length(which(bc.number==i))
  bc.bool<-c(bc.bool, bc.bool.curr)
}


#get target ion types
tg.ion.types <- unique(st[tg.rows,2])

#get all bg masses in a single vector
x<-c()
for(i in 3:max.fields){
  x<-c(x,st[bg.rows,i])
}
x<-na.omit(x)
bg.all<-x

# get the total number of transitions
bg.all.count<-length(bg.all)

#calculte the equal number of transitions 
x<-c()
for(i in 1:tg.ion.count){
  match.indices<-which(abs(bg.all - st[2,][[2+i]]) <= accuracy)
  x<-c(x, length(match.indices))
}

x.masses<-round(as.numeric(matrix(st[2,3:(2+tg.ion.count)], ncol=1)),1)
x.masses.labels<-paste(x.masses, "\nDa", sep="")

x.labels<-paste(tg.ion.types,1:tg.ion.count, sep="")
x<-data.frame(matrix(x, ncol=1), x.labels, 1:tg.ion.count, x.masses.labels, bc.bool)

colnames(x)<-c("value","name","id","mass","bc")

x$name = reorder(x$name, x$id)

p<-ggplot(data=x, aes(x=name, y=value, label=mass, fill=bc))
p <- p + geom_bar(stat = "identity")
#p <- p + scale_fill_gradient(legend=F)
p <- p + theme_bw()
p <- p + ylim(c(0, max(x$value) * 1.10))
p <- p + xlab("")
p <- p + ylab("frequency")
p <- p + geom_text(size=2.5, vjust=-0.5, angle=0)
p <- p + opts(axis.text.y=theme_text(angle=90))
p

# ggsave(filename=output, plot=p, width=10, height=5, dpi=72)

# dev.off()
