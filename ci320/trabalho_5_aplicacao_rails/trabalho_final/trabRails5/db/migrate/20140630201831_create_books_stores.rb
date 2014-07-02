class CreateBooksStores < ActiveRecord::Migration
  def change
    create_table :books_stores do |t|
      t.integer :book_id
      t.integer :store_id

      t.timestamps
    end
  end
end
