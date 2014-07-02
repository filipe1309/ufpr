class CreateEditions < ActiveRecord::Migration
  def change
    create_table :editions do |t|
      t.string :title
      t.integer :book_id
      t.integer :number

      t.timestamps
    end
  end
end
