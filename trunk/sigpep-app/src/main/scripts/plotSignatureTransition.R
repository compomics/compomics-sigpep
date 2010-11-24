#input file
file <- "/home/mmueller/Documents/my_phd/sienna2008/transition_example/ENSG00000058262-ETSMVHELNR.tsv"

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

#get target ion types
tg.ion.types <- unique(st[tg.rows,2])

#get target ion spectrum
target.spectrum <- st[tg.rows, c(3:max.fields)]
rownames(target.spectrum) <- st[tg.rows, 2]

#get background ion types
bg.ion.types <- unique(st[bg.rows,2])

#group background by ion type
bg.by.ion.type <- split(st[bg.rows,c(3:max.fields)], st[bg.rows,2])

bg.count <- length(bg.by.ion.type[[1]][,1])



#set barcode to plot
barcode.idx <- 1;

#get barcode
not.null <- which(st[barcode.idx,]>1)
barcode <- as.vector(st[barcode.idx,not.null])
barcode.col.names <- strsplit(as.character(barcode[2]),"-")
barcode <- barcode[c(3:length(barcode))]
colnames(barcode) <- barcode.col.names[[1]]

#sort barcode by m/z
ordered.barcode <- sort(as.double(barcode))

#sort background

bg.by.ion.type.ordered <- vector("list", length=length(bg.by.ion.type))
names(bg.by.ion.type.ordered) <- names(bg.by.ion.type)

for(ion.type in names(bg.by.ion.type)){

    bg <- bg.by.ion.type[[ion.type]]
    bg.count <- length(bg[,1])

    sort.order <- vector(length = 0, mode="integer")

    for(b in 1:length(ordered.barcode)){

        accuracy.min <- as.numeric(barcode[b]-accuracy)
        accuracy.max <- as.numeric(barcode[b]+accuracy)

        #get background peptide with product ion in m/z range
        greater.min.idx <- which(bg > accuracy.min, arr.ind=TRUE)
        bg.greater.min  <- cbind(bg[greater.min.idx], greater.min.idx[,1])

        smaller.max.idx <- which(bg.greater.min[,1] < accuracy.max)
        bg.in.range <- cbind(bg.greater.min[smaller.max.idx,1], bg.greater.min[smaller.max.idx,2])

        #get background indeces ordered by product ion m/z
        sort.result <- sort(bg.in.range[,1], index.return=TRUE, decreasing=TRUE)
        sort.idx <- sort.result$ix

        ordered.bg.idx <- bg.in.range[sort.idx,2]
        sort.order <- unique(c(sort.order,ordered.bg.idx))

    }

    sort.order <- unique(c(sort.order, c(1:bg.count)))

    bg.ordered <- bg[sort.order,]

    bg.by.ion.type.ordered[[ion.type]] <- bg.ordered

}

#get target spectrum
#not.null <- which(st[2,]>1)
#spectrum <- as.vector(st[2,not.null])

#plot creation
##############

png("/home/mmueller/Documents/my_phd/sienna2008/transition_example/ENSG00000058262-ETSMVHELNR.png", height=800, width=1000)

peptide.sequence="ETSMVHELNR"

barcode.color <- "black"
spectrum.color <- "black"
accuracy.color <- "lightgrey"

#rdbu7 color scheme
ion.color <- vector("list", length(6))
ion.color["x"] <- "#f1b6da"
ion.color["y"] <- "#de77ae"
ion.color["z"] <- "#fde0ef"
ion.color["a"] <- "#b8e186"
ion.color["b"] <- "#7fbc41"
ion.color["c"] <- "#e6f5d0"

#define plot layout
rows <- 2
columns <- length(barcode)
layout <- vector(mode="integer", length=2*columns)
for(c in 1:columns){
    layout[c] <- 1
    layout[c+columns] <- c+1
}

#intialise plot
layout(matrix(layout, rows, columns, byrow=TRUE), heights=c(0.5,0.5), widths=c(1))
plot(0,0, xlim=c(min.mz,max.mz), ylim=c(0,bg.count+100), xlab="m/z", ylab="background peptide index", pch="")

#plot(0,0, xlim=c(625,635), ylim=c(0,bg.count), xlab="m/z", ylab="peptide", pch="")

#plot background
for(ion.type in names(bg.by.ion.type.ordered)){
    col <- ion.color[[ion.type]]
    bg <- bg.by.ion.type.ordered[[ion.type]]
    not.null.idx <- which(bg > 1, arr.ind=TRUE)
    bg.plot  <- cbind(bg[not.null.idx], not.null.idx[,1])
    points(bg.plot, pch=".", col=col)
}

#draw spectrum
for(ion.type in rownames(target.spectrum)){
    abline(v=target.spectrum[ion.type,], col=spectrum.color, lty="dashed")
}

#draw barcode
abline(v=barcode, col=barcode.color)

#ion series labels
rect(min.mz,bg.count+1,max.mz,bg.count+200, col="white", border="white")

for(ion.type in rownames(target.spectrum)){
    spectrum <- target.spectrum[ion.type,]
    #mtext(paste(ion.type,c(1:length(spectrum))), at=spectrum, line=2)
    text(spectrum, bg.count+100, paste(ion.type,c(1:length(spectrum)), sep=""), bg="white")
}


#plot mass accuracy
#rect(barcode-0.5,1,barcode+0.5,bg.count, col=accuracy.color, border=accuracy.color)

square <- 22
ion.types <- names(bg.by.ion.type.ordered) 
legend.labels <- c(paste("background", ion.types, "ions"))
pt.bg <-  c(as.character(ion.color[ion.types]))
legend <- legend("bottomleft", legend.labels, cex=1.0, xjust=0, yjust=0, pch=rep(square, times=length(ion.types)), bg="white", pt.bg=pt.bg, bty="n", inset=0.05)


#create detailed plot for barcodes
for(b in 1:length(barcode)){

    ion.name <- names(barcode[b])

    xlim.min <- as.double(barcode[b]-accuracy-0.5)
    xlim.max <- as.double(barcode[b]+accuracy+0.5)

    #initialise plot
    plot(0,0, xlim=c(xlim.min,xlim.max), ylim=c(0,bg.count), xlab="m/z", ylab="background peptide index", pch="", main=ion.name)

    accuracy.min <- barcode[b]-accuracy
    accuracy.max <- barcode[b]+accuracy

    #draw mass accuracy
    rect(accuracy.min,1,accuracy.max,bg.count, col=accuracy.color, border=accuracy.color)

    #draw barcode
    abline(v=barcode[b], col=barcode.color)

    #plot background

    #get background in range
    for(ion.type in names(bg.by.ion.type)){
        col <- col2rgb(ion.color[[ion.type]])
        bg <- bg.by.ion.type.ordered[[ion.type]]

        greater.min.idx <- which(bg > xlim.min, arr.ind=TRUE)
        bg.greater.min  <- cbind(bg[greater.min.idx], greater.min.idx[,1])

        smaller.max.idx <- which(bg.greater.min[,1] < xlim.max)
        bg.in.range <- cbind(bg.greater.min[smaller.max.idx,1], bg.greater.min[smaller.max.idx,2])

        points(bg.in.range, pch=".", col=col)

    }


}

dev.off()
