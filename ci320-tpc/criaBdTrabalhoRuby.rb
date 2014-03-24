require 'active_record'


ActiveRecord::Base.establish_connection(
    :adapter => "sqlite3",
    :database  => "Livros.sqlite3"
)

ActiveRecord::Schema.define do
    create_table :books do |table|
        table.column :title, :string
        table.column :performer, :string
    end

    create_table :editions do |table|
        table.column :book_id, :integer
        table.column :edition_number, :integer
        table.column :title, :string
    end

    create_table :codes do |table|
        table.column :book_id, :integer
        table.column :name, :string
    end

   create_table :stores do |table|
        table.column :name, :string
    end

    create_table :books_stores do |table|
        table.column :book_id, :integer
        table.column :store_id, :integer
    end
end