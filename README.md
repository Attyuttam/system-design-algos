# system-design-algos
Implementing various system design algorithms that I come across

## LSM Tree 
Here I have implemented an LSM tree which is capable of the following:
1. Inserting into the memory(implemented using AVL tree)
2. Search a data (searches memory and if unavailable then searches in the disk)
3. Delete a data (searches memory and if unavailable then searches in the disk and deletes from the respective storage space)
3. There is a background process which does 2 activities:
    3.1 Transfer data from memory to disk (although I have not used any DB to represent a disk, but I intend to incorporate this in the near future)
    3.2 Merge the SS Tables in the disk, if that surpasses the provided limit

### Terms and Concepts

1. AVL Tree (Self balancing BST): https://en.wikipedia.org/wiki/AVL_tree
2. JobRunr: https://www.jobrunr.io/en/ (To run jobs in the bakcground)
3. MemTable: https://www.mauriciopoppe.com/notes/computer-science/data-structures/memtable-sstable/
4. SS Table: https://www.mauriciopoppe.com/notes/computer-science/data-structures/memtable-sstable/
5. Sparse Index: 
    Sparse indexing allows you to specify the conditions under which a pointer segment is suppressed, not generated, and put in the index database. Sparse indexing has two advantages. The primary one is that it reduces the size of the index, saving space and decreasing maintenance of the index.

### Available APIs
Please check com.systemdesign.algo.lsmtreealgo.infra.controller.StorageController

### Current issues
1. SS Table should be immutable, but I have made it mutable, need to fix that