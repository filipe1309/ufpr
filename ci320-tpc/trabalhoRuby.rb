require 'active_record'


ActiveRecord::Base.establish_connection(
    :adapter => "sqlite3",
    :database  => "Livros.sqlite3"
)

class Book < ActiveRecord::Base
    has_many :editions
    has_one :code , :dependent=>:destroy
    has_and_belongs_to_many :stores
end

class Edition < ActiveRecord::Base
    belongs_to :book 
end

class Code < ActiveRecord::Base
    belongs_to :book
end

class Store < ActiveRecord::Base
    has_and_belongs_to_many :books
end

class Books_stores < ActiveRecord::Base
end

# POPULATE
store0 = Store.create(:name => 'Store0')
store1 = Store.create(:name => 'Store1')
store2 = Store.create(:name => 'Store2')
store3 = Store.create(:name => 'Store3')
storeN = Store.create(:name => 'StoreN')

book = Book.create(:title => 'Black and Blue',
    :author => 'The Rolling Stones')
book.editions.create(:edition_number => 1, :title => 'Hot Stuff')
book.editions.create(:edition_number => 2, :title => 'Hand Of Fate')
book.editions.create(:edition_number => 3, :title => 'Cherry Oh Baby ')
book.editions.create(:edition_number => 4, :title => 'Memory Motel ')
book.editions.create(:edition_number => 5, :title => 'Hey Negrita')
book.editions.create(:edition_number => 6, :title => 'Fool To Cry')
book.editions.create(:edition_number => 7, :title => 'Crazy Mama')
book.editions.create(:edition_number => 8,
    :title => 'Melody (Inspiration By Billy Preston)')
book.create_code(:name => 'Name1')
book.stores.create(:name => 'Store1.1')
book.stores.create(:name => 'Store1.2')

book = Book.create(:title => 'Sticky Fingers',
    :author => 'The Rolling Stones')
book.editions.create(:edition_number => 1, :title => 'Brown Sugar')
book.editions.create(:edition_number => 2, :title => 'Sway')
book.editions.create(:edition_number => 3, :title => 'Wild Horses')
book.editions.create(:edition_number => 4,
    :title => 'Can\'t You Hear Me Knocking')
book.editions.create(:edition_number => 5, :title => 'You Gotta Move')
book.editions.create(:edition_number => 6, :title => 'Bitch')
book.editions.create(:edition_number => 7, :title => 'I Got The Blues')
book.editions.create(:edition_number => 8, :title => 'Sister Morphine')
book.editions.create(:edition_number => 9, :title => 'Dead Flowers')
book.editions.create(:edition_number => 10, :title => 'Moonlight Mile')
book.create_code(:name => 'Name2')

book.stores << store0
book.stores << store1
book.stores << store2

store1.name = "NewStore1"
store1.save
# /POPULATE
def submenu()
    puts "1 - Livro"
    puts "2 - Loja"
    puts "3 - Edicao"
    puts "0 - Menu"
    print "Digite o numero correspondente a uma das opcoes: "
    return gets.chomp
end

def addStores(book)
    addStores = true
    while(addStores)
        print "Digite o ID da Loja: "
        chosenStore = gets.chomp    
        chosenStore = Store.find(chosenStore.to_i)
        book.stores << chosenStore
        puts "Loja #{chosenStore.name} adicionada no livro #{book.title}" 
        puts "Adicionar mais lojas?"
        puts "0 - Nao"
        puts "1 - Sim"
        opcAdd = gets.chomp
        if(!opcAdd.eql? "1")
            addStores = false
        end
    end          
end 

def addBooks(newStore)
    addBooks = true
    while(addBooks)
        print "Digite o ID do Livro: "
        chosenBook = gets.chomp
        chosenBook = Book.find(chosenBook.to_i) 
        newStore.books << chosenBook
        puts "Livro #{chosenBook.title} adicionado na loja #{newStore.name}" 
        puts "Adicionar mais livros?"
        puts "0 - Nao"
        puts "1 - Sim"
        opcAdd = gets.chomp
        if(!opcAdd.eql? "1")
            addBooks = false
        end
    end        
end

def showEditions()
    allBooks = Book.all
    allBooks.each do |a|
        puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"                        
    end
    print "Digite o ID da Livro: "
    chosenBook = gets.chomp    
    chosenBook = Book.find(chosenBook.to_i)
    chosenBook.editions.each do |t|
        puts "EDICAO ID: #{t.id}, Titulo: #{t.title}, Paginas da edicao: #{t.edition_number}"
    end
    return chosenBook
end

quit = false
while(!quit)
    print "\e[H\e[2J"
    puts "1 - Insere"
    puts "2 - Altera"
    puts "3 - Exclui"
    puts "4 - Pesquisa"
    puts "0 - Sair"
    print "Digite o numero correspondente a uma das opcoes: "
    menu = gets.chomp   
    case menu
        when "1" 
            print "\e[H\e[2J"
            puts "<<<<<<<INSERCAO>>>>>>>"
            sub = submenu()
            case sub
                when "1"          	
                    insert = true
                    while(insert)
                        print "\e[H\e[2J"
                        #TODO Colocar opcao edition book
                        puts "<<<<<<<INSERCAO/LIVRO>>>>>>>"
                        print "Informe o titulo:"
                        title = gets.chomp
                        print "Informe o autor:"
                        author = gets.chomp
                        print "Informe o codigo(ISBN):"
                        bookCode = gets.chomp
                        book = Book.create(:title => title, :author => author)
                        book.create_code(:name => bookCode)
                        num_store = Store.count
                        puts num_store
                        if num_store != 0
                            puts "Escolha a loja:"
                            i = 0
                            while i < num_store
                                i+=1
                                a = Store.find(i)
                                puts "LOJA ID: #{a.id}, Name: #{a.name}"
                            end
                            addStores(book)                  
                        end
                        print "\e[H\e[2J"
                        puts "<<<<<<<INSERCAO/LIVRO>>>>>>>"
                        puts "1 - Inserir outro Livro"
                        puts "0 - Menu"
                        ret = gets.chomp
                        if(!ret.eql? "1")
                            insert = false
                        end
                    end
                when "2" 
                    insert = true
                    while(insert)
                        print "\e[H\e[2J"
                        puts "<<<<<<<INSERCAO/LOJA>>>>>>>"
                        print "Informe o nome:"
                        name = gets
                        print "\e[H\e[2J"
                        newStore = Store.create(:name => name)
                        num_books = Book.count
                        if num_books != 0
                            puts "Escolha um livro para ser adicionado a nova loja:"
                            i = 0
                            while i < num_books
                                i+=1
                                a = Book.find(i)
                                puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"
                            end 
                            addBooks(newStore)                                            
                        end
                        print "\e[H\e[2J"
                        puts "<<<<<<<INSERCAO/LOJA>>>>>>>"
                        puts "1 - Inserir outra Loja"
                        puts "2 - Adicionar livro existente a nova loja"
                        puts "0 - Menu"
                        ret = gets.chomp
                        if(!ret.eql? "1")
                            insert = false
                        end
                    end
                when "3" 
                    insert = true
                    while(insert)
                        print "\e[H\e[2J"
                        puts "<<<<<<<INSERCAO/EDICAO>>>>>>>"
                        num_books = Book.count
                        if num_books != 0
                            puts "Escolha o Livro:"
                            i = 0
                            while i < num_books
                                i+=1
                                a = Book.find(i)
                                puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"
                            end
                            puts "0 - Menu"
                            print "Digite o ID do Livro: "
                            chosenBook = gets.chomp
                            if chosenBook != "0"
                                book = Book.find(chosenBook.to_i)
                                puts "Edicoes atuais"
                                puts "============================================================================="
                                book.editions.each do |t|
                                    puts "EDITION ID: #{t.id}, Title: #{t.title}, Edition Number: #{t.edition_number}"
                                end
                                puts "============================================================================="
                                puts "Nova edicao"
                                print "Digite o titulo da edicao:"
                                title = gets.chomp
                                print "Digite o numero de paginas da edicao:"
                                num = gets.chomp
                                book.editions.create(:edition_number => num, :title => title)
                            end
                        else
                            puts "Operacao nao permitida, pois nao existem livros cadastrados."
                        end
                        print "\e[H\e[2J"
                        puts "<<<<<<<INSERCAO/EDICAO>>>>>>>"
                        puts "1 - Inserir outra edicao"
                        puts "0 - Menu"
                        ret = gets.chomp
                        if(!ret.eql? "1")
                            insert = false
                        end
                    end
                else 
                    print "\e[H\e[2J"
                    quit = true 
                    puts "Opcao invalida"
            end
        when "2" 
            print "\e[H\e[2J"
            puts "<<<<<<<ALTERACAO>>>>>>>"
            sub = submenu()
            case sub 
                when "1"
                    change = true
                    while(change)
                        print "\e[H\e[2J"
                        puts "<<<<<<<ALTERACAO/LIVRO>>>>>>>"
                        num_books = Book.count
                        if num_books != 0
                            i = 0
                            while i < num_books
                                i+=1
                                a = Book.find(i)
                                puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"
                                puts "============================================================================="
                            end
                            print "Digite o ID do Livro: "
                            chosenBook = gets.chomp
                            book = Book.find(chosenBook.to_i)
                            puts "Escolha um campo para alterar:"
                            puts "0 - Titulo"
                            puts "1 - Autor"
                            puts "2 - Lojas"
                            puts "3 - Codigo(ISBN)"
                            optChange = gets.chomp
                            if(optChange.eql? "0")
                                print "Digite o novo titulo:"
                                newTitle = gets.chomp
                                book.title = newTitle                                
                            elsif(optChange.eql? "1")
                                print "Digite o novo autor:"
                                newAuthor = gets.chomp
                                book.author = newAuthor                                
                            elsif(optChange.eql? "2")
                                puts "============================================================================="                                                         
                                puts "Todas as lojas:"
                                objStores = Store.all
                                objStores.each do |tb|
                                    puts "LOJA ID: #{tb.id}, Nome: #{tb.name}"
                                end   
                                puts "============================================================================="                         
                                puts "Lojas que contem este livro:"
                                book.stores.each do |t|
                                    puts "LOJA ID: #{t.id}, Nome: #{t.name}"
                                end
                                puts "============================================================================="                                                         
                                puts "0 - Adicionar nova loja ao livro"
                                puts "1 - Remover loja existente ao livro"
                                opcStore = gets.chomp
                                if(opcStore.eql? "0")
                                    addStores(book)
                                elsif(opcStore.eql? "1")
                                    print "Digite o ID da Loja:"
                                    chosenStore = gets.chomp    
                                    chosenStore = Store.find(chosenStore.to_i)                             
                                    bs = Books_stores.delete_all("store_id = #{chosenStore.id} AND book_id = #{book.id}")                                    
                                end                                                                
                            elsif(optChange.eql? "3")
                                puts "Codigo Atual: #{book.code.name}"
                                print "Digite o novo Codigo(ISBN):"
                                newName = gets.chomp
                                cd = Code.find_by_name(book.code.name)
                                Code.update(cd.id,name: newName)                
                            end
                            book.save
                        else
                            puts "Operacao nao permitida, pois nao existem livros cadastrados."                    
                        end
                        puts "<<<<<<<ALTERACAO/LIVRO>>>>>>>"
                        puts "1 - Alterar outro livro?"
                        puts "0 - Menu"
                        ret = gets.chomp
                        if(!ret.eql? "1")
                            change = false
                        end
                    end
                when "2"
                    change = true
                    while(change)
                        print "\e[H\e[2J"
                        puts "<<<<<<<ALTERACAO/LOJA>>>>>>>"
                        num_stores = Store.count
                        if num_stores != 0
                            i = 0
                            while i < num_stores
                                i+=1
                                a = Store.find(i)
                                puts "LOJA ID: #{a.id}, Nome: #{a.name}"
                                puts "============================================================================="
                            end
                            print "Digite o ID da loja:"
                            chosenStore = gets.chomp
                            chosenStore = Store.find(chosenStore.to_i)
                            puts "Escolha um campo para alterar:"
                            puts "0 - Nome"
                            puts "1 - Livros"
                            optChange = gets.chomp
                            if(optChange.eql? "0")
                                print "Digite o novo nome:"
                                newName = gets.chomp
                                chosenStore.name = newName                              
                            elsif(optChange.eql? "1")
                                puts "============================================================================="                                                         
                                puts "Todas os livros:"
                                objBooks = Book.all
                                objBooks.each do |tb|
                                    puts "LIVRO ID: #{tb.id}, Titulo: #{tb.title}, Autor: #{tb.author}, ISBN: #{tb.code.name}"                                    
                                end   
                                puts "============================================================================="                         
                                puts "Livros que estao nesta loja:"
                                chosenStore.books.each do |tb|
                                    puts "LIVRO ID: #{tb.id}, Titulo: #{tb.title}, Autor: #{tb.author}, ISBN: #{tb.code.name}"                
                                end
                                puts "============================================================================="                                                         
                                puts "0 - Adicionar novo livro a loja"
                                puts "1 - Remover livro existente da loja"
                                opcStore = gets.chomp
                                if(opcStore.eql? "0")
                                    addBooks(chosenStore)
                                elsif(opcStore.eql? "1")
                                    print "Digite o ID da Livro: "
                                    chosenBook = gets.chomp    
                                    chosenBook = Book.find(chosenBook.to_i)                             
                                    bs = Books_stores.delete_all("store_id = #{chosenStore.id} AND book_id = #{chosenBook.id}")                                    
                                end                                                                             
                            end
                            chosenStore.save
                        else
                            puts "Operacao nao permitida, pois nao existem lojas cadastrados."                    
                        end
                        puts "<<<<<<<ALTERACAO/LOJA>>>>>>>"
                        puts "1 - Alterar outra loja?"
                        puts "0 - Menu"
                        ret = gets.chomp
                        if(!ret.eql? "1")
                            change = false
                        end
                    end
                when "3"
                    print "\e[H\e[2J"
                    puts "<<<<<<<ALTERACAO/EDICAO>>>>>>>"
                    chosenBook = showEditions()
                    print "Digite o ID da edicao:"
                    chosenEdition = gets.chomp
                    chosenEdition = chosenBook.editions.find(chosenEdition.to_i)
                    puts "============================================================================="
                    puts "Alterando #{chosenEdition.title}:"
                    puts "0 - Titulo"
                    puts "1 - Numero de Paginas"
                    opcEdition = gets.chomp
                    if(opcEdition.eql? "0")
                        print "Digite o titulo da edicao:"
                        title = gets.chomp
                        Edition.update(chosenEdition.id,title: title)
                    elsif(opcEdition.eql? "1")
                        print "Digite o numero de paginas da edicao:"
                        num = gets.chomp
                        Edition.update(chosenEdition.id,edition_number: num.to_i)
                    end
                else
            end
                    
        when "3" 
            print "\e[H\e[2J"
            puts "<<<<<<<EXCLUSAO>>>>>>>"
            sub = submenu()
            case sub 
                when "1"
                    puts "<<<<<<<EXCLUSAO/LIVRO>>>>>>>"
                    allBooks = Book.all
                    allBooks.each do |a|
                        puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"                        
                    end
                    print "Digite o ID do Livro: "
                    chosenBook = gets.chomp   
                    book = Book.find(chosenBook.to_i)
                    book.destroy
                when "2"
                    puts "<<<<<<<EXCLUSAO/LOJA>>>>>>>"
                    allStores = Store.all
                    allStores.each do |tb|
                        puts "LOJA ID: #{tb.id}, Nome: #{tb.name}"
                    end
                    print "Digite o ID da Loja:"
                    chosenStore = gets.chomp   
                    delStore = Store.find(chosenStore.to_i)
                    delStore.destroy
                when "3"
                    puts "<<<<<<<EXCLUSAO/EDICAO>>>>>>>" 
                    allBooks = Book.all
                    allBooks.each do |a|
                        puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"                        
                    end
                    print "Digite o ID da Livro: "
                    chosenBook = gets.chomp    
                    chosenBook = Book.find(chosenBook.to_i)
                    chosenBook.editions.each do |t|
                        puts "EDICAO ID: #{t.id}, Titulo: #{t.title}, Paginas da edicao: #{t.edition_number}"
                    end
                    print "Digite o ID da Edicao: " 
                    chosenEdition = gets.chomp
                    cs = Edition.find(chosenEdition.to_i)
                    cs.destroy               
            end

     	when "4" 
            print "\e[H\e[2J"
            puts "<<<<<<<PESQUISA>>>>>>>"
            sub = submenu()
            case sub
            	when "1"
            		allBooks = Book.all
                    allBooks.each do |a|
                        puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"
                        a.editions.each do |t|
                            puts "EDICAO ID: #{t.id}, Titulo: #{t.title}, Paginas da edicao: #{t.edition_number}"
                        end
                        a.stores.each do |t|
                            puts "LOJA ID: #{t.id}, Nome: #{t.name}"
                        end
                        puts "============================================================================="                        
                    end
                    #puts "Albums-Stores"
                    #bs = Books_stores.all
                    #bs.each do |tb|
                    #    puts "#{tb.book_id} #{tb.store_id}"
                    #end
				when "2"
					puts "<<<<<<<LOJAS>>>>>>>"
                    allStores = Store.all
                    allStores.each do |tb|
                        puts "LOJA ID: #{tb.id}, Nome: #{tb.name}"
                        tb.books.each do |a|
                            puts "  LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"                        
                        end
                        puts "============================================================================="                        
                    end
				when "3"
					puts "<<<<<<<EDICOES>>>>>>>"
                    allBooks = Book.all
                    allBooks.each do |a|
                        puts "LIVRO ID: #{a.id}, Titulo: #{a.title}, Autor: #{a.author}, ISBN: #{a.code.name}"                        
                    end
                    print "Digite o ID da Livro: "
                    chosenBook = gets.chomp    
                    chosenBook = Book.find(chosenBook.to_i)
                    chosenBook.editions.each do |t|
                        puts "EDICAO ID: #{t.id}, Titulo: #{t.title}, Paginas da edicao: #{t.edition_number}"
                    end
				else
					puts "---"
			end
            
        else 
            print "\e[H\e[2J"
            quit = true 
            puts "good bye"
    end
    puts "###########################"    
    puts "      PRESSIONE ENTER"
    puts "###########################"
    w8 = gets
end
